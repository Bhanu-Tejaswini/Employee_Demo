package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.example.demo.model.VSBaseModel;

@Entity
@Table(name = "permission")
public class Permission extends VSBaseModel {

	@Column(name = "name",unique=true)
	private String name;

	public Permission() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
