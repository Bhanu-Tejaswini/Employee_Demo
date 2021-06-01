package com.arraigntech.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "update_title")
public class UpdateTitleEntity extends VSBaseModel {

	@Column
	private String title;
	@Column(nullable=true)
	private String description;
	@JsonIgnore
	@OneToOne(targetEntity = ChannelEntity.class)
	private ChannelEntity channel;
	
	public UpdateTitleEntity() {
		
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

	public ChannelEntity getChannel() {
		return channel;
	}

	public void setChannel(ChannelEntity channel) {
		this.channel = channel;
	}
}
