package com.arraigntech.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "streams")
public class Streams extends VSBaseModel {

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

	@ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL)
	private User user;
	
	@OneToMany(mappedBy="stream")
	private List<StreamTarget> streamTarget=new ArrayList();

	public Streams() {

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<StreamTarget> getStreamTarget() {
		return streamTarget;
	}

	public void setStreamTarget(List<StreamTarget> streamTarget) {
		this.streamTarget = streamTarget;
	}
}
