package com.arraigntech.wowza.response.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DirectPlaybackUrlsVO {

	@JsonProperty("webrtc")
	private List<WebrtcVO> webrtc;

	public List<WebrtcVO> getWebrtc() {
		return webrtc;
	}

	public void setWebrtc(List<WebrtcVO> webrtc) {
		this.webrtc = webrtc;
	}

	@Override
	public String toString() {
		return "DirectPlaybackUrls [webrtc=" + webrtc + "]";
	}
}
