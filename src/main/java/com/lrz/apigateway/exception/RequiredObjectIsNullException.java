package com.lrz.apigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequiredObjectIsNullException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public RequiredObjectIsNullException(String exception) {
		super(exception);
	}
	public RequiredObjectIsNullException() {
		super("Null Objects cannot be persisted");
	}
	
}
