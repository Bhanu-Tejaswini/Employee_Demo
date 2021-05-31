package com.arraigntech.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.arraigntech.exceptions.Model.ErrorResponse;
import com.arraigntech.model.response.BaseResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final String MESSAGE = "message";

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(AppException.class)
	public BaseResponse<ErrorResponse> handleAppException(AppException e) {
		LOGGER.error(e.getMessage(), e);
		BaseResponse<ErrorResponse> response = new BaseResponse<ErrorResponse>();
		response.withResponseMessage(MESSAGE, e.getMessage());
		response.withSuccess(false);
		return response;
	}
}
