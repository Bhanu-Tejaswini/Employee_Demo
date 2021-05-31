/**
 * 
 */
package com.arraigntech.service;

import java.util.List;

import com.arraigntech.entity.Channels;
import com.arraigntech.entity.User;
import com.arraigntech.model.ChannelDTO;
import com.arraigntech.model.ChannelErrorUIResponse;
import com.arraigntech.model.ChannelListUIResponse;
import com.arraigntech.model.ChannelStatus;
import com.arraigntech.model.CustomChannelDTO;
import com.arraigntech.model.UpdateAllTitleDTO;
import com.arraigntech.model.UpdateTitleDTO;
import com.arraigntech.utility.ChannelTypeProvider;

/**
 * @author Bhaskara S
 *
 */
public interface ChannelService {
	public boolean createChannel(ChannelDTO channelDTO);
	public boolean addFaceBookChannel(ChannelDTO channelDTO);
	public boolean addInstagramChannel(CustomChannelDTO customChannelDTO);
	public String execute(String accessToken);
	public boolean removechannel(String channelId);
	public String enableChannel(ChannelStatus channelStatus);
	public String disableChannel(ChannelStatus channelStatus);
	public List<Channels>findByUserAndTypeAndActive(User newUser, ChannelTypeProvider type,boolean active);
	public ChannelListUIResponse getUserChannels();
	public boolean validChannel(Channels channel);
	public List<ChannelErrorUIResponse> getErrorChannels();
	public boolean addUpdateTitle(UpdateTitleDTO updateTitleDTO);
	public UpdateTitleDTO getUpdateTitle(String channelId);
	public boolean updateAllTitles(UpdateAllTitleDTO updateAllTitleDTO);
}
