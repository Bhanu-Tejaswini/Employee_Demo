package com.arraigntech.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelCDN {

	@JsonProperty("ingestionInfo")
	public ChannelIngestionInfo ingestionInfo;

	public ChannelIngestionInfo getIngestionInfo() {
		return ingestionInfo;
	}

	public void setIngestionInfo(ChannelIngestionInfo ingestionInfo) {
		this.ingestionInfo = ingestionInfo;
	}
}
