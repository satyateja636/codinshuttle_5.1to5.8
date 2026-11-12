package com.codingshuttle.SecurityApp.SecurityApplication.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingshuttle.SecurityApp.SecurityApplication.dto.LoginDTO;
import com.codingshuttle.SecurityApp.SecurityApplication.dto.SignUpDTO;
import com.codingshuttle.SecurityApp.SecurityApplication.dto.UserDto;
import com.codingshuttle.SecurityApp.SecurityApplication.services.AuthService;
import com.codingshuttle.SecurityApp.SecurityApplication.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
 
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;
	private final AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<UserDto> signUp(@RequestBody SignUpDTO signUpDto) {
		UserDto userDto = userService.signUp(signUpDto);
		
		return ResponseEntity.ok(userDto);
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDTO loginDto, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
		
		String token = authService.login(loginDto);
		
		//if we want we can add the cookie like below for request or to get the cookie in the response
		Cookie cookie = new Cookie("refreshToken", token);
		cookie.setHttpOnly(true);
		httpServletResponse.addCookie(cookie);
		return ResponseEntity.ok(token);
		
	}
}
