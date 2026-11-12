package com.codingshuttle.SecurityApp.SecurityApplication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;
import com.codingshuttle.SecurityApp.SecurityApplication.services.JwtService;

@SpringBootTest
class SecurityApplicationTests {

	@Autowired
	private JwtService jwtService;
	
	
	
	@Test
	void contextLoads() {
		
		User user = new User(4L, "satya@gmail.com", "1234");
		
		String token = jwtService.generateToken(user);
		//to verify this token we can use jwt.io by pasting this token there in that website
		System.out.println(token);
		Long id = jwtService.getUserIdFromToken(token);
		
		System.out.println(id);
	}

}
