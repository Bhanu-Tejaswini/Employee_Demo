package com.arraigntech.model;

import com.arraigntech.utility.AuthenticationProvider;

public class ChannelDTO {
	private AuthenticationProvider account;
	private String channelName;
	private String channelUrl;

	public AuthenticationProvider getAccount() {
		return account;
	}

	public void setAccount(AuthenticationProvider account) {
		this.account = account;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelUrl() {
		return channelUrl;
	}

	public void setChannelUrl(String channelUrl) {
		this.channelUrl = channelUrl;
	}

	public ChannelDTO(AuthenticationProvider account, String channelName, String channelUrl) {
		super();
		this.account = account;
		this.channelName = channelName;
		this.channelUrl = channelUrl;
	}

}
