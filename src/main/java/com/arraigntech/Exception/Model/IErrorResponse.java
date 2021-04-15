package com.example.demo.Exception.Model;

import java.io.Serializable;

public interface IErrorResponse extends Serializable {
	
	String getType();

	String getErrorCode();

	String getMessage();

}
