package com.arraigntech.request;

import java.util.List;

import com.arraigntech.utility.AuthenticationProvider;

public class UserVO {
	private String username = "";
	private String email;
	private String password;
	private List<String> role;
	private AuthenticationProvider provider;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getRole() {
		return role;
	}

	public void setRole(List<String> role) {
		this.role = role;
	}

	public AuthenticationProvider getProvider() {
		return provider;
	}

	public void setProvider(AuthenticationProvider provider) {
		this.provider = provider;
	}

	public UserVO(String username, String email, String password, List<String> role, AuthenticationProvider provider) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
		this.provider = provider;
	}
	
	public UserVO() {
		
	}

	@Override
	public String toString() {
		return "UserDTO [username=" + username + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", provider=" + provider + "]";
	}
}
