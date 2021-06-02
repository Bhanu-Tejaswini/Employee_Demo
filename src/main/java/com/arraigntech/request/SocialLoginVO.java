package com.arraigntech.request;

public class SocialLoginVO {

	private String email;
	private String username;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "SocialLoginDTO [email=" + email + ", username=" + username + "]";
	}
}
