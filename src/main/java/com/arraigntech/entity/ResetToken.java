package com.arraigntech.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "reset_token")
public class ResetToken extends VSBaseModel {

	@Column
	private String token;
	@OneToOne(targetEntity = User.class)
	private User user;

	public ResetToken() {

	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ResetToken(String token, User user) {
		super();
		this.token = token;
		this.user = user;
	}

}
