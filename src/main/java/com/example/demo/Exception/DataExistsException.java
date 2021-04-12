package com.example.demo.Exception;

public class DataExistsException extends Exception {
	static final long serialVersionUID = 1L;
		public DataExistsException(String message) {
			super(message);
		}
}
