package com.arraigntech.streamsModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OutputStreamTargetDTO {

	@JsonProperty("output_stream_target")
	private OutputStreamTarget outputStreamTarget;

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

}
