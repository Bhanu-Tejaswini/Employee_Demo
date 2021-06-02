package com.arraigntech.request;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelVO {

	@JsonProperty("items")
	private List<ChannelItemsVO> items = new ArrayList<>();

	private String graphDomain;

	private String accessToken;

	@JsonProperty("userID")
	private String userId;

	public List<ChannelItemsVO> getItems() {
		return items;
	}

	public void setItems(List<ChannelItemsVO> items) {
		this.items = items;
	}

	public String getGraphDomain() {
		return graphDomain;
	}

	public void setGraphDomain(String graphDomain) {
		this.graphDomain = graphDomain;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "ChannelDTO [items=" + items + ", graphDomain=" + graphDomain + ", accessToken=" + accessToken
				+ ", userId=" + userId + "]";
	}
}
