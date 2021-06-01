/**
 * 
 */
package com.arraigntech.response.vo;

import java.util.List;

import com.arraigntech.entity.ChannelEntity;

/**
 * @author Bhaskara S
 *
 */
public class ChannelListUIResponseVO {

	private List<ChannelUIResponseVO> channelList;
	private boolean flag;

	public List<ChannelUIResponseVO> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<ChannelUIResponseVO> channelList) {
		this.channelList = channelList;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public ChannelListUIResponseVO(List<ChannelUIResponseVO> channelsList, boolean flag) {
		super();
		this.channelList = channelsList;
		this.flag = flag;
	}

	public ChannelListUIResponseVO() {

	}

	@Override
	public String toString() {
		return "ChannelListUIResponse [channelList=" + channelList + ", flag=" + flag + "]";
	}
}
