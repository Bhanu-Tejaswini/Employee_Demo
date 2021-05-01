package com.arraigntech.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelDTO {

	@JsonProperty("items")
	private List<ChannelItems> items=new ArrayList<>();

	public List<ChannelItems> getItems() {
		return items;
	}

	public void setItems(List<ChannelItems> items) {
		this.items = items;
	}

}
