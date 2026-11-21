package com.codingshuttle.SecurityApp.SecurityApplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.codingshuttle.SecurityApp.SecurityApplication.entities.enums.Permission;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.enums.Role;
import com.codingshuttle.SecurityApp.SecurityApplication.filters.JwtAuthFilter;
import com.codingshuttle.SecurityApp.SecurityApplication.handlers.OAuth2SuccessHandler;

import lombok.RequiredArgsConstructor;

//if we want to configure our own security filter instead of the existing security filters we will use the below class
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		httpSecurity
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/posts", "/error", "/public/**", "/auth/**", "/home.html").permitAll()
						.requestMatchers(HttpMethod.GET, "/posts").permitAll()
						.requestMatchers(HttpMethod.POST, "/posts/**").hasAnyRole(Role.ADMIN.name() , Role.CREATOR.name())
						.requestMatchers(HttpMethod.POST, "/posts/**").hasAnyAuthority(Permission.POST_CREATE.name())
						.requestMatchers(HttpMethod.GET, "/posts/**").hasAnyAuthority(Permission.POST_VIEW.name())
						.requestMatchers(HttpMethod.PUT, "/posts/**").hasAuthority(Permission.POST_UPDATE.name())
						.requestMatchers(HttpMethod.DELETE, "/posts/**").hasAuthority(Permission.POST_DELETE.name())
						.anyRequest().authenticated())
				.csrf(csrfConfig -> csrfConfig.disable())  //by default csrf will enabled, if we want to disable it we do like this
				.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.oauth2Login(oauth2Config -> oauth2Config.failureUrl("/login?error=true").successHandler(oAuth2SuccessHandler));  
//				.formLogin(Customizer.withDefaults());

		return httpSecurity.build();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
//	@Bean
//	UserDetailsService myInMemoryUserDetailsService() {
//		UserDetails normalUser = User.withUsername("satya").password(passwordEncoder().encode("Satya123")).roles("USER").build();
//		UserDetails adminUser = User.withUsername("admin").password(passwordEncoder().encode("admin")).roles("ADMIN").build();
//		
//		return new InMemoryUserDetailsManager(normalUser, adminUser);
//	}

}
