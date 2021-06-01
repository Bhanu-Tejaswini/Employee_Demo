/**
 * 
 */
package com.arraigntech.wowza.request.vo;

/**
 * @author Bhaskara S
 *
 */
public class FacebookStreamRequestVO {

	private String streamName;
	private String primaryUrl;

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

	public String getPrimaryUrl() {
		return primaryUrl;
	}

	public void setPrimaryUrl(String primaryUrl) {
		this.primaryUrl = primaryUrl;
	}

	@Override
	public String toString() {
		return "FacebookStreamRequest [streamName=" + streamName + ", primaryUrl=" + primaryUrl + "]";
	}
}
