package com.arraigntech.streamsModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LiveStreamState {

	@JsonProperty("live_stream")
	State liveStreamState;

	public State getLiveStreamState() {
		return liveStreamState;
	}

	public void setLiveStreamState(State liveStreamState) {
		this.liveStreamState = liveStreamState;
	}

}
