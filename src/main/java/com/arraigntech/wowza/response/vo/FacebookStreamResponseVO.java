package com.arraigntech.wowza.response.vo;

/**
 * @author Bhaskara S
 *
 */
public class FacebookStreamResponseVO {

	private String stream_url;

	public String getStream_url() {
		return stream_url;
	}

	public void setStream_url(String stream_url) {
		this.stream_url = stream_url;
	}

	@Override
	public String toString() {
		return "FacebookStreamResponse [stream_url=" + stream_url + "]";
	}
}