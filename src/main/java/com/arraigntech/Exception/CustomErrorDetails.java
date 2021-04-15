package com.example.demo.Exception;

import java.util.Date;

public class CustomErrorDetails {

	private Date date;

	private String message;

	private String errorDetails;

	public CustomErrorDetails(Date date, String message, String errorDetails) {
		super();
		this.date = date;
		this.message = message;
		this.errorDetails = errorDetails;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

}