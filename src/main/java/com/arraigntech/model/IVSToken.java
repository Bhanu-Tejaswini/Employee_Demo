package com.arraigntech.model;

import java.util.ArrayList;
import java.util.List;

public class IVSToken {

	private List<String> aud = new ArrayList<>();
	private String user_name;
	private List<String> scope = new ArrayList<>();
	private int exp;
	private List<String> authorities = new ArrayList<>();
	private String jti;
	private String email;
	private String client_id;

	public IVSToken() {

	}

	public List<String> getAud() {
		return aud;
	}

	public void setAud(List<String> aud) {
		this.aud = aud;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public List<String> getScope() {
		return scope;
	}

	public void setScope(List<String> scope) {
		this.scope = scope;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	public String getJti() {
		return jti;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

}
