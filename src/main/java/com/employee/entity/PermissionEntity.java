package com.employee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "permission")
public class PermissionEntity {

	@Column(name = "name",unique=true)
	private String name;

	public PermissionEntity() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
