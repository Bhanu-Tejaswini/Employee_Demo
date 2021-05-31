package com.arraigntech.streams.model;

public class StreamUIResponse {
	private String sdpUrl;
	private String applicationName;
	private String streamName;
	private String streamId;
	private String streamStatus;

	public String getSdpUrl() {
		return sdpUrl;
	}

	public void setSdpUrl(String sdpUrl) {
		this.sdpUrl = sdpUrl;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

	public String getStreamId() {
		return streamId;
	}

	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}

	public String getStreamStatus() {
		return streamStatus;
	}

	public void setStreamStatus(String streamStatus) {
		this.streamStatus = streamStatus;
	}

	public StreamUIResponse() {

	}

	public StreamUIResponse(String sdpUrl, String applicationName, String streamName, String streamId,
			String streamStatus) {
		super();
		this.sdpUrl = sdpUrl;
		this.applicationName = applicationName;
		this.streamName = streamName;
		this.streamId = streamId;
		this.streamStatus = streamStatus;
	}
}
