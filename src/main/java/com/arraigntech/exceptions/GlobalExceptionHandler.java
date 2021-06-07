package com.arraigntech.exceptions;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.arraigntech.exceptions.model.ErrorResponse;
import com.arraigntech.response.vo.BaseResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final String MESSAGE = "message";

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(AppException.class)
	public BaseResponse<ErrorResponse> handleAppException(AppException e) {
		BaseResponse<ErrorResponse> response = new BaseResponse<ErrorResponse>();
		response.withResponseMessage(MESSAGE, e.getMessage());
		response.withSuccess(false);
		return response;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(FileSizeLimitExceededException.class)
	public BaseResponse<ErrorResponse> handleFileSizeLimitExceededException(AppException e) {
		BaseResponse<ErrorResponse> response = new BaseResponse<ErrorResponse>();
		response.withResponseMessage(MESSAGE, e.getMessage());
		response.withSuccess(false);
		return response;
	}
}
