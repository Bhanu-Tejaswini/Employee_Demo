package com.example.demo.Exception;

import java.util.Date;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public CustomErrorDetails handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
			WebRequest request) {
		return new CustomErrorDetails(new Date(), "Arguments Not valid", ex.getMessage());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public CustomErrorDetails handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex,
			WebRequest request) {
		return new CustomErrorDetails(new Date(), " Http request method not supported for the request",
				ex.getMessage());
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CustomErrorDetails handleMissingServletRequestParameterException(MissingServletRequestParameterException ex,
			WebRequest request) {
		return new CustomErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public CustomErrorDetails handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
		return new CustomErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
	}

	@ExceptionHandler(ResponseStatusException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CustomErrorDetails handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
		return new CustomErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
	}

	@ExceptionHandler(DataNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public CustomErrorDetails handleUserNotFoundException(DataNotFoundException ex, WebRequest request) {
		return new CustomErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
	}

	@ExceptionHandler(DataExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public CustomErrorDetails handleUserExistsException(DataExistsException ex, WebRequest request) {
		return new CustomErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
	}

}
