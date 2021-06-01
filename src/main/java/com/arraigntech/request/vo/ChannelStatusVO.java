package com.arraigntech.request.vo;

public class ChannelStatusVO {
	
	private String channelId;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public String toString() {
		return "ChannelStatus [channelId=" + channelId + "]";
	}
}
