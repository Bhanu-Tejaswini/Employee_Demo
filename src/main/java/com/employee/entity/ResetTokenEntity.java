package com.employee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "reset_token")
public class ResetTokenEntity {

	@Column
	private String token;
	@OneToOne(targetEntity = EmployeeEntity.class)
	private EmployeeEntity user;

	public ResetTokenEntity() {

	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public EmployeeEntity getUser() {
		return user;
	}

	public void setUser(EmployeeEntity user) {
		this.user = user;
	}

	public ResetTokenEntity(String token, EmployeeEntity user) {
		super();
		this.token = token;
		this.user = user;
	}

}
