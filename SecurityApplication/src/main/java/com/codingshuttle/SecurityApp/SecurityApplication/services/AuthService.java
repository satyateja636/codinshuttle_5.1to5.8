package com.codingshuttle.SecurityApp.SecurityApplication.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.codingshuttle.SecurityApp.SecurityApplication.dto.LoginDTO;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthenticationManager authenticatioManager;
	private final JwtService jwtService;
	
	public String login(LoginDTO loginDto) {

		Authentication authentication = authenticatioManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
		User user = (User)authentication.getPrincipal();
		String token = jwtService.generateToken(user);
		
		return token;
	}
}
