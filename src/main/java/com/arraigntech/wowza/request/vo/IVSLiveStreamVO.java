package com.arraigntech.wowza.request.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IVSLiveStreamVO {
		
	@JsonProperty("live_stream")
	LiveStreamVO live_stream;

	public LiveStreamVO getLiveStream() {
		return live_stream;
	}

	public void setLiveStream(LiveStreamVO live_stream) {
		this.live_stream = live_stream;
	}

	@Override
	public String toString() {
		return "IVSLiveStream [live_stream=" + live_stream + "]";
	}
}
