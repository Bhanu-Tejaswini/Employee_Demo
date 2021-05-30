package com.arraigntech.model;

public class LoginResponseDTO {

	private String result;
	private boolean flag;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public LoginResponseDTO(String result, boolean flag) {
		super();
		this.result = result;
		this.flag = flag;
	}

	public LoginResponseDTO() {
		
	}
}
