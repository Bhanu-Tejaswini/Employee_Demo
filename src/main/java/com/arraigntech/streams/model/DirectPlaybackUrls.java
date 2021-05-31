package com.arraigntech.streams.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DirectPlaybackUrls {

	@JsonProperty("webrtc")
	private List<Webrtc> webrtc;

	public List<Webrtc> getWebrtc() {
		return webrtc;
	}

	public void setWebrtc(List<Webrtc> webrtc) {
		this.webrtc = webrtc;
	}

}
