package com.arraigntech.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Authority {
	private String authority;

	@JsonProperty("attributes")
	private Attributes attributes;

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}
}
