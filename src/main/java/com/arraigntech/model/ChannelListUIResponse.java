/**
 * 
 */
package com.arraigntech.model;

import java.util.List;

import com.arraigntech.entity.Channels;

/**
 * @author Bhaskara S
 *
 */
public class ChannelListUIResponse {

	private List<ChannelUIResponse> channelList;
	private boolean flag;

	public List<ChannelUIResponse> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<ChannelUIResponse> channelList) {
		this.channelList = channelList;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public ChannelListUIResponse(List<ChannelUIResponse> channelsList, boolean flag) {
		super();
		this.channelList = channelsList;
		this.flag = flag;
	}

	public ChannelListUIResponse() {

	}

	@Override
	public String toString() {
		return "ChannelListUIResponse [channelList=" + channelList + ", flag=" + flag + "]";
	}
}
