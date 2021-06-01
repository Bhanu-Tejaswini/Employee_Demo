package com.arraigntech.wowza.response.vo;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "stream_target")
public class StreamTargetRootVO {

	@Field
	@JsonProperty("stream_target_custom")
	private StreamTargetVO streamTarget;

	private String streamId;

	public StreamTargetVO getStreamTarget() {
		return streamTarget;
	}

	public void setStreamTarget(StreamTargetVO streamTarget) {
		this.streamTarget = streamTarget;
	}

	public String getStreamId() {
		return streamId;
	}

	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}

	@Override
	public String toString() {
		return "StreamTargetDTO [streamTarget=" + streamTarget + ", streamId=" + streamId + "]";
	}
}
