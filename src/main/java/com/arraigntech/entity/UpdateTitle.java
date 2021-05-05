package com.arraigntech.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "update_title")
public class UpdateTitle extends VSBaseModel {

	@Column
	private String title;
	@Column(nullable=true)
	private String description;
	@JsonIgnore
	@OneToOne(targetEntity = Channels.class)
	private Channels channel;
	
	public UpdateTitle() {
		
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

	public Channels getChannel() {
		return channel;
	}

	public void setChannel(Channels channel) {
		this.channel = channel;
	}
}
