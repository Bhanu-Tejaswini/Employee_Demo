package com.arraigntech.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "channels")
public class Channels extends VSBaseModel {

	// stores social media account name
	@Column
	private String account;

	@Column(name = "channel_name")
	private String channelName;

	@Column(name = "channel_url")
	private String channelUrl;

	@Column(name = "active")
	@Type(type = "numeric_boolean")
	private boolean active;

	@ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL)
	private User user;

	public Channels() {

	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
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
