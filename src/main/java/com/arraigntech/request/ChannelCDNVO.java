package com.arraigntech.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelCDNVO {

	@JsonProperty("ingestionInfo")
	public ChannelIngestionInfoVO ingestionInfo;

	public ChannelIngestionInfoVO getIngestionInfo() {
		return ingestionInfo;
	}

	public void setIngestionInfo(ChannelIngestionInfoVO ingestionInfo) {
		this.ingestionInfo = ingestionInfo;
	}

	@Override
	public String toString() {
		return "ChannelCDN [ingestionInfo=" + ingestionInfo + "]";
	}

}
