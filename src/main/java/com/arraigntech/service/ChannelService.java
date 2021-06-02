/**
 * 
 */
package com.arraigntech.service;

import java.util.List;

import com.arraigntech.entity.ChannelEntity;
import com.arraigntech.entity.UserEntity;
import com.arraigntech.request.ChannelStatusVO;
import com.arraigntech.request.ChannelVO;
import com.arraigntech.request.CustomChannelVO;
import com.arraigntech.request.UpdateAllTitleVO;
import com.arraigntech.response.ChannelErrorUIResponseVO;
import com.arraigntech.response.ChannelListUIResponseVO;
import com.arraigntech.response.UpdateTitleVO;
import com.arraigntech.utility.ChannelTypeProvider;

/**
 * @author Bhaskara S
 *
 */
public interface ChannelService {
	public boolean createChannel(ChannelVO channelDTO);
	public boolean addFaceBookChannel(ChannelVO channelDTO);
	public boolean addInstagramChannel(CustomChannelVO customChannelDTO);
	public String execute(String accessToken);
	public boolean removechannel(String channelId);
	public String enableChannel(ChannelStatusVO channelStatus);
	public String disableChannel(ChannelStatusVO channelStatus);
	public List<ChannelEntity>findByUserAndTypeAndActive(UserEntity newUser, ChannelTypeProvider type,boolean active);
	public ChannelListUIResponseVO getUserChannels();
	public boolean validChannel(ChannelEntity channel);
	public List<ChannelErrorUIResponseVO> getErrorChannels();
	public boolean addUpdateTitle(UpdateTitleVO updateTitleDTO);
	public UpdateTitleVO getUpdateTitle(String channelId);
	public boolean updateAllTitles(UpdateAllTitleVO updateAllTitleDTO);
}
