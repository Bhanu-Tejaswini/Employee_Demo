package com.arraigntech.streamsModel;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.arraigntech.entity.User;
import com.arraigntech.entity.VSBaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "streams")
public class IVSLiveStreamResponse extends VSBaseModel {

	@Field
	@JsonProperty("live_stream")
	LiveStreamResponse liveStreamResponse;
	private User user;

	public LiveStreamResponse getLiveStreamResponse() {
		return liveStreamResponse;
	}

	public void setLiveStreamResponse(LiveStreamResponse liveStreamResponse) {
		this.liveStreamResponse = liveStreamResponse;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
