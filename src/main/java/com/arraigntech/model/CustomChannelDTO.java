/**
 * 
 */
package com.arraigntech.model;

/**
 * @author Bhaskara S
 *
 */
public class CustomChannelDTO {

	private String channelType;
	private String streamKey;
	private String rtmpUrl;

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getStreamKey() {
		return streamKey;
	}

	public void setStreamKey(String streamKey) {
		this.streamKey = streamKey;
	}

	public String getRtmpUrl() {
		return rtmpUrl;
	}

	public void setRtmpUrl(String rtmpUrl) {
		this.rtmpUrl = rtmpUrl;
	}

}
