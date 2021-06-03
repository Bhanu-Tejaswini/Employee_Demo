package com.arraigntech.request.vo;

public class IVSResetPasswordVO {
	
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "IVSResetPassword [password=" + password + "]";
	}
}
