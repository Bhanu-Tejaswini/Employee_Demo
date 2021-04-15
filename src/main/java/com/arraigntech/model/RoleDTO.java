package com.example.demo.model;

import java.util.List;

public class RoleDTO {

	private String name;
	private List<String> permission;

	public RoleDTO() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getPermission() {
		return permission;
	}

	public void setPermission(List<String> permission) {
		this.permission = permission;
	}

}
