package com.arraigntech.streams.model;

public class StreamSourceConnectionInformation {

	private String sdp_url;
	private String application_name;
	private String stream_name;

	public String getSdp_url() {
		return sdp_url;
	}

	public void setSdp_url(String sdp_url) {
		this.sdp_url = sdp_url;
	}

	public String getApplication_name() {
		return application_name;
	}

	public void setApplication_name(String application_name) {
		this.application_name = application_name;
	}

	public String getStream_name() {
		return stream_name;
	}

	public void setStream_name(String stream_name) {
		this.stream_name = stream_name;
	}

	@Override
	public String toString() {
		return "StreamSourceConnectionInformation [sdp_url=" + sdp_url + ", application_name=" + application_name
				+ ", stream_name=" + stream_name + "]";
	}

}
