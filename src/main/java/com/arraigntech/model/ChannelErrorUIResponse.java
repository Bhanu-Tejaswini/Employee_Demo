/**
 * 
 */
package com.arraigntech.model;

import com.arraigntech.utility.ChannelTypeProvider;

/**
 * @author Bhaskara S
 *
 */
public class ChannelErrorUIResponse {

	private ChannelTypeProvider provider;
	private String channelId;

	public ChannelTypeProvider getProvider() {
		return provider;
	}

	public void setProvider(ChannelTypeProvider provider) {
		this.provider = provider;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public ChannelErrorUIResponse(ChannelTypeProvider provider, String channelId) {
		super();
		this.provider = provider;
		this.channelId = channelId;
	}

	public ChannelErrorUIResponse() {
		
	}

	@Override
	public String toString() {
		return "ChannelErrorUIResponse [provider=" + provider + ", channelId=" + channelId + "]";
	}
}
