package com.arraigntech.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelCdn {

	@JsonProperty("ingestionInfo")
	public ChannelIngestionInfo ingestionInfo;

	public ChannelIngestionInfo getIngestionInfo() {
		return ingestionInfo;
	}

	public void setIngestionInfo(ChannelIngestionInfo ingestionInfo) {
		this.ingestionInfo = ingestionInfo;
	}
}
