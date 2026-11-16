package com.codingshuttle.SecurityApp.SecurityApplication.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.codingshuttle.SecurityApp.SecurityApplication.exceptions.ResourceNotFoundException;

@RestControllerAdvice	
public class GlobalExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ApiError> handeResourceNotFoundException(ResourceNotFoundException exception){
		ApiError apiError = new ApiError(exception.getLocalizedMessage(), HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
	}
}
