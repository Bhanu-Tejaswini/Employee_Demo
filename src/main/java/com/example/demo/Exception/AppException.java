package com.example.demo.Exception;

import com.example.demo.Exception.Model.IErrorResponse;

/**
 * 
 * @author tulabandula.kumar
 *
 */

public class AppException extends RuntimeException {
	
	private static final String GLOBAL_ERROR_CODE = "E0001";
	private static final long serialVersionUID = 1L;

	private final String errorCode;

	private IErrorResponse errorResponse = null;

	/**
	 * Constructor
	 */
	public AppException() {
		this.errorCode = GLOBAL_ERROR_CODE;
	}
	
	/**
	 * Parameterised Constructor to take message
	 *
	 * @param message
	 */
	public AppException(String message) {
		super(message);
		this.errorCode = GLOBAL_ERROR_CODE;
	}
	
	/**
	 * Parameterized constructor to take error code and message
	 *
	 * @param errorCode
	 * @param message
	 */
	public AppException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * Parameterised Constructor to take custom message and root cause
	 *
	 * @param message
	 * @param cause
	 */
	public AppException(String message, Throwable cause) {
		super(message, cause);
		this.errorCode = GLOBAL_ERROR_CODE;
	}

	/**
	 * Parameterised Constructor to take error code, custom message and root
	 * cause
	 *
	 * @param message
	 * @param cause
	 */
	public AppException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	/**
	 * Parameterised Constructor to do exception chainning
	 *
	 * @param cause
	 */
	public AppException(Throwable cause) {
		super(cause);
		this.errorCode = GLOBAL_ERROR_CODE;
	}

	/**
	 * Overridding the default constructor
	 *
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public AppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.errorCode = GLOBAL_ERROR_CODE;
	}

	public AppException(IErrorResponse errorResponse) {
		super();
		this.errorCode = errorResponse.getErrorCode();
		this.errorResponse = errorResponse;
	}

	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @return the errorResponse
	 */
	public IErrorResponse getErrorResponse() {
		return errorResponse;
	}


}
