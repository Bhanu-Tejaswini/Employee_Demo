/**
 * 
 */
package com.arraigntech.model;

import com.arraigntech.entity.UpdateTitle;
import com.arraigntech.utility.ChannelTypeProvider;

/**
 * @author Bhaskara S
 *
 */
public class ChannelUIResponse {
	private String username;
	private ChannelTypeProvider type;
	private String channelId;
	private Object streamName;
	private Object primaryUrl;
	private Object backupUrl;
	private boolean active;
	private String accessToken;
	private String facebookUserId;
	private UpdateTitle updateTitle;

	public ChannelTypeProvider getType() {
		return type;
	}

	public void setType(ChannelTypeProvider type) {
		this.type = type;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Object getStreamName() {
		return streamName;
	}

	public void setStreamName(Object streamName) {
		this.streamName = streamName;
	}

	public Object getPrimaryUrl() {
		return primaryUrl;
	}

	public void setPrimaryUrl(Object primaryUrl) {
		this.primaryUrl = primaryUrl;
	}

	public Object getBackupUrl() {
		return backupUrl;
	}

	public void setBackupUrl(Object backupUrl) {
		this.backupUrl = backupUrl;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getFacebookUserId() {
		return facebookUserId;
	}

	public void setFacebookUserId(String facebookUserId) {
		this.facebookUserId = facebookUserId;
	}

	public UpdateTitle getUpdateTitle() {
		return updateTitle;
	}

	public void setUpdateTitle(UpdateTitle updateTitle) {
		this.updateTitle = updateTitle;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ChannelUIResponse(String username, ChannelTypeProvider type, String channelId, Object streamName,
			Object primaryUrl, Object backupUrl, boolean active, String accessToken, String facebookUserId,
			UpdateTitle updateTitle) {
		super();
		this.username = username;
		this.type = type;
		this.channelId = channelId;
		this.streamName = streamName;
		this.primaryUrl = primaryUrl;
		this.backupUrl = backupUrl;
		this.active = active;
		this.accessToken = accessToken;
		this.facebookUserId = facebookUserId;
		this.updateTitle = updateTitle;
	}

	public ChannelUIResponse() {

	}

	@Override
	public String toString() {
		return "ChannelUIResponse [username=" + username + ", type=" + type + ", channelId=" + channelId
				+ ", streamName=" + streamName + ", primaryUrl=" + primaryUrl + ", backupUrl=" + backupUrl + ", active="
				+ active + ", accessToken=" + accessToken + ", facebookUserId=" + facebookUserId + ", updateTitle="
				+ updateTitle + "]";
	}
}
