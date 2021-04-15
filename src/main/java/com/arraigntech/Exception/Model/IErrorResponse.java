package com.arraigntech.Exception.Model;

import java.io.Serializable;

public interface IErrorResponse extends Serializable {
	
	String getType();

	String getErrorCode();

	String getMessage();

}
