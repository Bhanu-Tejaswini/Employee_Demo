package com.arraigntech.streams.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "stream_target")
public class StreamTargetDTO {

	@Field
	@JsonProperty("stream_target_custom")
	private StreamTargetModel streamTarget;

	private String streamId;

	public StreamTargetModel getStreamTarget() {
		return streamTarget;
	}

	public void setStreamTarget(StreamTargetModel streamTarget) {
		this.streamTarget = streamTarget;
	}

	public String getStreamId() {
		return streamId;
	}

	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}
}
