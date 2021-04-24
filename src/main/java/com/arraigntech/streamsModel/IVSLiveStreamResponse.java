package com.arraigntech.streamsModel;



import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.arraigntech.entity.VSBaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection="streams")
public class IVSLiveStreamResponse extends VSBaseModel {

	@Field
	@JsonProperty("live_stream")
	LiveStreamResponse liveStreamResponse;

	public LiveStreamResponse getLiveStreamResponse() {
		return liveStreamResponse;
	}

	public void setLiveStreamResponse(LiveStreamResponse liveStreamResponse) {
		this.liveStreamResponse = liveStreamResponse;
	}
}
