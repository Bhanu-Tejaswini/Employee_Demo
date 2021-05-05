package com.arraigntech.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.arraigntech.utility.AuthenticationProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "channels")
public class Channels extends VSBaseModel {

	// stores social media account name
	@Enumerated(EnumType.STRING)
	@Column
	private AuthenticationProvider type;

	@Column(name = "channel_id")
	private String channelId;

	@Column(name = "active")
	@Type(type = "numeric_boolean")
	private boolean active;
	
	@Column(name="access_token", nullable=true)
	private String accessToken;

	@JsonIgnore
	@ManyToOne(targetEntity = User.class)
	private User user;

	@OneToOne(mappedBy="channel",cascade=CascadeType.ALL)
	private UpdateTitle updateTitle;
	public Channels() {

	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UpdateTitle getUpdateTitle() {
		return updateTitle;
	}

	public void setUpdateTitle(UpdateTitle updateTitle) {
		this.updateTitle = updateTitle;
	}

	public AuthenticationProvider getType() {
		return type;
	}

	public void setType(AuthenticationProvider type) {
		this.type = type;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	

}
