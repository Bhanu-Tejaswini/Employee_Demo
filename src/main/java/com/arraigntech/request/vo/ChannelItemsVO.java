package com.arraigntech.request.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelItemsVO {

	private String kind;
	@JsonProperty("snippet")
	private ChannelSnippetVO snippet;
	@JsonProperty("cdn")
	private ChannelCDNVO cdn;

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public ChannelSnippetVO getSnippet() {
		return snippet;
	}

	public void setSnippet(ChannelSnippetVO snippet) {
		this.snippet = snippet;
	}

	public ChannelCDNVO getCdn() {
		return cdn;
	}

	public void setCdn(ChannelCDNVO cdn) {
		this.cdn = cdn;
	}

	@Override
	public String toString() {
		return "ChannelItems [kind=" + kind + ", snippet=" + snippet + ", cdn=" + cdn + "]";
	}

}
