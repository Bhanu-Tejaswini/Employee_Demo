package com.arraigntech.Exception.Model;

/**
 * 
 * @author tulabandula.kumar
 *
 */

public class ErrorResponse  implements IErrorResponse {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2950205714230650728L;

	private String errorCode;

	private String message;

	public ErrorResponse() {
		super();
	}

	public ErrorResponse(String errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;

	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getType() {
		return ErrorResponseType.DEFAULT;
	}



}
