package com.employee.response.vo;

public class LoginResponseVO {

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

	public LoginResponseVO(String result, boolean flag) {
		super();
		this.result = result;
		this.flag = flag;
	}

	public LoginResponseVO() {
		
	}

	@Override
	public String toString() {
		return "LoginResponseDTO [result=" + result + ", flag=" + flag + "]";
	}
}
