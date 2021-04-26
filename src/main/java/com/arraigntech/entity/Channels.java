package com.arraigntech.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
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
	private AuthenticationProvider account;

	@Column(name = "channel_name")
	private String channelName;

	@Column(name = "channel_url")
	private String channelUrl;

	@Column(name = "active")
	@Type(type = "numeric_boolean")
	private boolean active;

	@JsonIgnore
	@ManyToOne(targetEntity = User.class)
	private User user;

	public Channels() {

	}

	public AuthenticationProvider getAccount() {
		return account;
	}

	public void setAccount(AuthenticationProvider account) {
		this.account = account;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelUrl() {
		return channelUrl;
	}

	public void setChannelUrl(String channelUrl) {
		this.channelUrl = channelUrl;
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

}
