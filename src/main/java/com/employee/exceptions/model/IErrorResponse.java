package com.employee.exceptions.model;

import java.io.Serializable;

public interface IErrorResponse extends Serializable {
	
	String getType();

	String getErrorCode();

	String getMessage();

}
