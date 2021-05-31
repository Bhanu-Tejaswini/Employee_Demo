package com.arraigntech.streams.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "output_stream_target")
public class OutputStreamTargetDTO {

	@Field
	@JsonProperty("output_stream_target")
	private OutputStreamTarget outputStreamTarget;
	private String streamId;

	public OutputStreamTarget getOutputStreamTarget() {
		return outputStreamTarget;
	}

	public void setOutputStreamTarget(OutputStreamTarget outputStreamTarget) {
		this.outputStreamTarget = outputStreamTarget;
	}

	public OutputStreamTargetDTO(OutputStreamTarget outputStreamTarget) {
		super();
		this.outputStreamTarget = outputStreamTarget;
	}

	public OutputStreamTargetDTO() {
	}

	public String getStreamId() {
		return streamId;
	}

	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}
}
