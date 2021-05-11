package com.arraigntech.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelItems {

	private String kind;
	@JsonProperty("snippet")
	private ChannelSnippet snippet;
	@JsonProperty("cdn")
	private ChannelCdn cdn;

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public ChannelSnippet getSnippet() {
		return snippet;
	}

	public void setSnippet(ChannelSnippet snippet) {
		this.snippet = snippet;
	}

	public ChannelCdn getCdn() {
		return cdn;
	}

	public void setCdn(ChannelCdn cdn) {
		this.cdn = cdn;
	}

}
