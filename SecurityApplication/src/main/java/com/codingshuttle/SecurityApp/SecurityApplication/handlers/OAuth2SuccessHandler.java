package com.codingshuttle.SecurityApp.SecurityApplication.handlers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;
import com.codingshuttle.SecurityApp.SecurityApplication.services.JwtService;
import com.codingshuttle.SecurityApp.SecurityApplication.services.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	
	@Value("${deploy.env}")
	private String deployEnv;
	
	private final UserService userService;
	private final JwtService jwtService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException{
		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
		DefaultOAuth2User oauth2User = (DefaultOAuth2User) token.getPrincipal();
		
		
		String email = oauth2User.getAttribute("email");
		User user = userService.getUserByEmail(email);
		
		if(user == null) {
			User newUser = User.builder().name(oauth2User.getAttribute("name")).email(email).build();
			user = userService.saveUser(newUser);
		}
		
		String accessToken = jwtService.generateAccessToken(user);
		String refreshToken = jwtService.generateRefreshToken(user);
		
		Cookie cookie = new Cookie("refreshToken", refreshToken);
		cookie.setHttpOnly(true);
		cookie.setSecure("production".equals(deployEnv));
		httpServletResponse.addCookie(cookie);
		
		String frontEndUrl = "http://localhost:8080/home.html?token="+accessToken;
		
		httpServletResponse.sendRedirect(frontEndUrl);
		
	}
}
