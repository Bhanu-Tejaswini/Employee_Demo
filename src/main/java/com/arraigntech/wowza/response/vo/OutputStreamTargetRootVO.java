package com.arraigntech.wowza.response.vo;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "output_stream_target")
public class OutputStreamTargetRootVO {

	@Field
	@JsonProperty("output_stream_target")
	private OutputStreamTargetVO outputStreamTarget;
	private String streamId;

	public OutputStreamTargetVO getOutputStreamTarget() {
		return outputStreamTarget;
	}

	public void setOutputStreamTarget(OutputStreamTargetVO outputStreamTarget) {
		this.outputStreamTarget = outputStreamTarget;
	}

	public OutputStreamTargetRootVO(OutputStreamTargetVO outputStreamTarget) {
		super();
		this.outputStreamTarget = outputStreamTarget;
	}

	public OutputStreamTargetRootVO() {
	}

	public String getStreamId() {
		return streamId;
	}

	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}

	@Override
	public String toString() {
		return "OutputStreamTargetDTO [outputStreamTarget=" + outputStreamTarget + ", streamId=" + streamId + "]";
	}
}
