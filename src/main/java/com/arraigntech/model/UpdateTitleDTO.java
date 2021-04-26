package com.arraigntech.model;

import com.arraigntech.utility.AuthenticationProvider;

public class UpdateTitleDTO {

	private AuthenticationProvider account;
	private String title;
	private String description;

	public AuthenticationProvider getAccount() {
		return account;
	}

	public void setAccount(AuthenticationProvider account) {
		this.account = account;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
