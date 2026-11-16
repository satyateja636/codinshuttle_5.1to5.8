package com.codingshuttle.SecurityApp.SecurityApplication.controllers;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingshuttle.SecurityApp.SecurityApplication.dto.LoginDTO;
import com.codingshuttle.SecurityApp.SecurityApplication.dto.LoginResponseDto;
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
	
	@Value("${display.env}")
	private String deployEnv;

	@PostMapping("/signup")
	public ResponseEntity<UserDto> signUp(@RequestBody SignUpDTO signUpDto) {
		UserDto userDto = userService.signUp(signUpDto);

		return ResponseEntity.ok(userDto);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDTO loginDto, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {

		LoginResponseDto loginResponseDto = authService.login(loginDto);

		// if we want we can add the cookie like below for request or to get the cookie
		// in the response
		Cookie cookie = new Cookie("refreshToken", loginResponseDto.getRefreshToken());
		cookie.setHttpOnly(true);
		cookie.setSecure("production".equals(deployEnv));
		httpServletResponse.addCookie(cookie);
		return ResponseEntity.ok(loginResponseDto);

	}

	@PostMapping("/refreshToken")
	public ResponseEntity<LoginResponseDto> refreshToken(HttpServletRequest httpServletRequest) {
		String refreshToken = Arrays.stream(httpServletRequest.getCookies()).filter(cookie -> "refreshToken".equals(cookie.getName()))
				.findFirst().map(cookie -> cookie.getValue() )
				.orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the cookies"));
		
		LoginResponseDto loginResponseDto = authService.refreshToken(refreshToken);
		
		return ResponseEntity.ok(loginResponseDto);

	}
}
