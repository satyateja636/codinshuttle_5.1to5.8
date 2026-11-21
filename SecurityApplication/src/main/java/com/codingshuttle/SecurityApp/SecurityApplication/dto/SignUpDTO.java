package com.codingshuttle.SecurityApp.SecurityApplication.dto;

import java.util.Set;

import com.codingshuttle.SecurityApp.SecurityApplication.entities.enums.Permission;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.enums.Role;

import lombok.Data;

@Data
public class SignUpDTO {

	private String email;
	private String password;
	private String name;
	private Set<Role> roles;
	private Set<Permission> permissions;
}
