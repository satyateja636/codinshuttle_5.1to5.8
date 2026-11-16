package com.codingshuttle.SecurityApp.SecurityApplication.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.codingshuttle.SecurityApp.SecurityApplication.dto.LoginDTO;
import com.codingshuttle.SecurityApp.SecurityApplication.dto.LoginResponseDto;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthenticationManager authenticatioManager;
	private final JwtService jwtService;
	private final UserService userService;
	
	
	public LoginResponseDto login(LoginDTO loginDto) {

		Authentication authentication = authenticatioManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
		User user = (User)authentication.getPrincipal();
		String token = jwtService.generateToken(user);
		String accessToken = jwtService.generateAccessToken(user);
		String refreshToken = jwtService.generateRefreshToken(user);
//		return token;
		
		return new LoginResponseDto(user.getId(), accessToken, refreshToken);
	}

	public LoginResponseDto refreshToken(String refreshToken) {

		Long userId = jwtService.getUserIdFromToken(refreshToken);
		User user = userService.getUserById(userId);
		
		String accessToken = jwtService.generateAccessToken(user);
		return new LoginResponseDto(user.getId(), accessToken, refreshToken);
		
	}
}
