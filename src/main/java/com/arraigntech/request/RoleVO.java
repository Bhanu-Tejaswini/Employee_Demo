package com.arraigntech.request;

import java.util.List;

public class RoleVO {

	private String name;
	private List<String> permission;

	public RoleVO() {

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
