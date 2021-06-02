package com.arraigntech.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "reset_token")
public class ResetTokenEntity extends VSBaseModel {

	@Column
	private String token;
	@OneToOne(targetEntity = UserEntity.class)
	private UserEntity user;

	public ResetTokenEntity() {

	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public ResetTokenEntity(String token, UserEntity user) {
		super();
		this.token = token;
		this.user = user;
	}

}
