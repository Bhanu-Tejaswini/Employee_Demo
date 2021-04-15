package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IVSLiveStream {
		
	@JsonProperty("live_stream")
	LiveStream live_stream;

	public LiveStream getLiveStream() {
		return live_stream;
	}

	public void setLiveStream(LiveStream live_stream) {
		this.live_stream = live_stream;
	}
}
