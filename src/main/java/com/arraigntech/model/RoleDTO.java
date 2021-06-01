package com.arraigntech.model;

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

	@Override
	public String toString() {
		return "RoleDTO [name=" + name + ", permission=" + permission + "]";
	}
}
