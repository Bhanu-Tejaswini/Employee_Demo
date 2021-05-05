package com.arraigntech.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelDTO {

	@JsonProperty("items")
	private List<ChannelItems> items = new ArrayList<>();

	private String graphDomain;

	private String accessToken;

	public List<ChannelItems> getItems() {
		return items;
	}

	public void setItems(List<ChannelItems> items) {
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
}
