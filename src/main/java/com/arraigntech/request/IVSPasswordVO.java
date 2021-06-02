package com.arraigntech.request;

public class IVSPasswordVO extends IVSResetPasswordVO {

	private String newpassword;

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

	@Override
	public String toString() {
		return "IVSPassword [password=" + getPassword() + ", newpassword=" + newpassword + "]";
	}
}
