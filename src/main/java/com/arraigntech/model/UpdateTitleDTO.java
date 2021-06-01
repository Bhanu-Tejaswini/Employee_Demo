package com.arraigntech.model;

public class UpdateTitleDTO {

	private String channelId;
	private String title;
	private String description;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public UpdateTitleDTO()
	{
		
	}

	public UpdateTitleDTO(String channelId, String title, String description) {
		super();
		this.channelId = channelId;
		this.title = title;
		this.description = description;
	}

	@Override
	public String toString() {
		return "UpdateTitleDTO [channelId=" + channelId + ", title=" + title + ", description=" + description + "]";
	}
}
