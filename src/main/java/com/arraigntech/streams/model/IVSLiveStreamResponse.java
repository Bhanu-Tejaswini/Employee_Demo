package com.arraigntech.streams.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.arraigntech.entity.User;
import com.arraigntech.entity.VSBaseModel;
import com.arraigntech.model.MongoUser;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "streams")
public class IVSLiveStreamResponse extends VSBaseModel {

	@Field
	@JsonProperty("live_stream")
	LiveStreamResponse liveStreamResponse;
	private MongoUser user;

	public LiveStreamResponse getLiveStreamResponse() {
		return liveStreamResponse;
	}

	public void setLiveStreamResponse(LiveStreamResponse liveStreamResponse) {
		this.liveStreamResponse = liveStreamResponse;
	}

	public MongoUser getUser() {
		return user;
	}

	public void setUser(MongoUser user) {
		this.user = user;
	}

}
