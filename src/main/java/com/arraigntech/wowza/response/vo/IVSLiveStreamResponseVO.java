package com.arraigntech.wowza.response.vo;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.arraigntech.entity.VSBaseModel;
import com.arraigntech.response.MongoUserVO;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "streams")
public class IVSLiveStreamResponseVO extends VSBaseModel {

	@Field
	@JsonProperty("live_stream")
	LiveStreamResponseVO liveStreamResponse;
	private MongoUserVO user;

	public LiveStreamResponseVO getLiveStreamResponse() {
		return liveStreamResponse;
	}

	public void setLiveStreamResponse(LiveStreamResponseVO liveStreamResponse) {
		this.liveStreamResponse = liveStreamResponse;
	}

	public MongoUserVO getUser() {
		return user;
	}

	public void setUser(MongoUserVO user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "IVSLiveStreamResponse [liveStreamResponse=" + liveStreamResponse + ", user=" + user + "]";
	}
}
