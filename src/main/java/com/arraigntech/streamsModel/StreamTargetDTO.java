package com.arraigntech.streamsModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StreamTargetDTO {

	@JsonProperty("stream_target_custom")
	private StreamTarget streamTarget;

	public StreamTarget getStreamTarget() {
		return streamTarget;
	}

	public void setStreamTarget(StreamTarget streamTarget) {
		this.streamTarget = streamTarget;
	}

}
