package com.arraigntech.wowza.response.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LiveStreamStateVO {

	@JsonProperty("live_stream")
	StateVO liveStreamState;

	public StateVO getLiveStreamState() {
		return liveStreamState;
	}

	public void setLiveStreamState(StateVO liveStreamState) {
		this.liveStreamState = liveStreamState;
	}

	@Override
	public String toString() {
		return "LiveStreamState [liveStreamState=" + liveStreamState + "]";
	}
}
