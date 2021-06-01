package com.arraigntech.model;

public class UpdateAllTitleDTO {

	private String title;
	private String description;

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

	public UpdateAllTitleDTO(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}
	
	public UpdateAllTitleDTO() {
		
	}

	@Override
	public String toString() {
		return "UpdateAllTitleDTO [title=" + title + ", description=" + description + "]";
	}
}
