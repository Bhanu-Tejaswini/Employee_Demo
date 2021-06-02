package com.arraigntech.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "streams")
public class StreamEntity extends VSBaseModel {

	@Column(name = "stream_id", unique = true)
	private String streamId;

	@Column(name = "stream_name", unique = true)
	private String streamName;

	@Column
	@Type(type = "numeric_boolean")
	private boolean active;

	@Column(name = "stream_url", unique = true)
	private String streamUrl;

	@Column(name = "application_name")
	private String applicationName;

	@Column(name = "source_stream_name")
	private String sourceStreamName;

	@ManyToOne(targetEntity = UserEntity.class)
	private UserEntity user;
	
	@OneToMany(mappedBy="stream")	
	private List<StreamTargetEntity> streamTarget = new ArrayList();

	public StreamEntity() {

	}

	public String getStreamId() {
		return streamId;
	}

	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getStreamUrl() {
		return streamUrl;
	}

	public void setStreamUrl(String streamUrl) {
		this.streamUrl = streamUrl;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getSourceStreamName() {
		return sourceStreamName;
	}

	public void setSourceStreamName(String sourceStreamName) {
		this.sourceStreamName = sourceStreamName;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public List<StreamTargetEntity> getStreamTarget() {
		return streamTarget;
	}

	public void setStreamTarget(List<StreamTargetEntity> streamTarget) {
		this.streamTarget = streamTarget;
	}
}
