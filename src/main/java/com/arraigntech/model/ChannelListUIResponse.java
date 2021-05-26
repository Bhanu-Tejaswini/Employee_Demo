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

	private List<Channels> channelList;
	private boolean flag;

	public List<Channels> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<Channels> channelList) {
		this.channelList = channelList;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public ChannelListUIResponse(List<Channels> channelList, boolean flag) {
		super();
		this.channelList = channelList;
		this.flag = flag;
	}

	public ChannelListUIResponse() {

	}

}
