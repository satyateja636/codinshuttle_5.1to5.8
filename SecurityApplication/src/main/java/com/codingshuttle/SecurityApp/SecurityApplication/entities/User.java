package com.codingshuttle.SecurityApp.SecurityApplication.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.codingshuttle.SecurityApp.SecurityApplication.entities.enums.Permission;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.enums.Role;
import com.codingshuttle.SecurityApp.SecurityApplication.utils.PermissionMapping;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class User implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String email;
	private String password;
	private String name;
	
	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<Role>  roles;
	
	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<Permission> permissions;
	
	//authorities are nothing but the role in the context of spring security
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
//		roles.forEach(role -> {
//			Set<SimpleGrantedAuthority> permissions = PermissionMapping.getAuthoritiesForRole(role);
//			authorities.addAll(permissions);
//			authorities.add(new SimpleGrantedAuthority("ROLE_"+role.name()));
//		});
//		return authorities;
//	}
//	
	
	//above commented of getAuthorities is from coding shuttle but getting some errors, so asked for help to the chatgpt and provided me with the below code
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    Set<SimpleGrantedAuthority> authorities = new HashSet<>();
	    // Add roles
	    roles.forEach(role ->
	        authorities.add(
	            new SimpleGrantedAuthority("ROLE_" + role.name())
	        )
	    );
	    // Add directly assigned permissions
	    permissions.forEach(permission ->
	        authorities.add(
	            new SimpleGrantedAuthority(permission.name())
	        )
	    );
	    return authorities;
	}
	
	@Override
	public @Nullable String getPassword() {
		return this.password;
	}
	
	@Override
	public String getUsername() {
		return this.email;
	}
	

}
