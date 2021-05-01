package com.arraigntech.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.arraigntech.Exception.AppException;
import com.arraigntech.entity.Channels;
import com.arraigntech.entity.UpdateTitle;
import com.arraigntech.entity.User;
import com.arraigntech.model.ChannelDTO;
import com.arraigntech.model.ChannelStatus;
import com.arraigntech.model.UpdateTitleDTO;
import com.arraigntech.repository.ChannelsRepository;
import com.arraigntech.repository.UpdateTitleRepository;
import com.arraigntech.repository.UserRespository;
import com.arraigntech.utility.AuthenticationProvider;
import com.arraigntech.utility.CommonUtils;
import com.arraigntech.utility.MessageConstants;

@Service
public class ChannelServiceImpl {
	
	@Autowired
	private ChannelsRepository channelRepo;
	
	@Autowired
	private UpdateTitleRepository updateTitleRepo;
	
	@Autowired
	private UserRespository userRepo;

	
	private User getUser() {
		User user = userRepo.findByEmail(CommonUtils.getUser());
		if (Objects.isNull(user)) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		return user;
	}
	
	public boolean createChannel(ChannelDTO channelDTO) {
		if(channelDTO.getItems().isEmpty()|| channelDTO.getItems().get(0).getId()==null) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		Channels channel=channelRepo.findByChannelId(channelDTO.getItems().get(0).getId());
		if(Objects.nonNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_EXISTS);
		}
		
		AuthenticationProvider channelAccount = null;
		if(channelDTO.getItems().get(0).getKind().startsWith("youtube")) {
			channelAccount=AuthenticationProvider.YOUTUBE;
		}else if(channelDTO.getItems().get(0).getKind().startsWith("facebook")) {
			channelAccount=AuthenticationProvider.FACEBOOK;
		}
		Channels channels = new Channels();
		User newUser=getUser();
		channels.setAccount(channelAccount);
		channels.setChannelId(channelDTO.getItems().get(0).getId());
		channels.setActive(true);
		channels.setUser(newUser);
		channelRepo.save(channels);
		return true;		
	}
	
	public boolean removechannel(String channelId) {
		if(!StringUtils.hasText(channelId)){
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		Channels newchannel=channelRepo.findByChannelId(channelId);
		if(Objects.isNull(newchannel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channelRepo.delete(newchannel);
		return true;
	}
	
	public String enableChannel(ChannelStatus channelStatus) {
		Channels channel=channelRepo.findByChannelId(channelStatus.getChannelId());
		if(Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channel.setActive(true);
		channelRepo.save(channel);
		return MessageConstants.CHANNEL_ENABLED;
	}
	
	public String disableChannel(ChannelStatus channelStatus) {
		Channels channel=channelRepo.findByChannelId(channelStatus.getChannelId());
		if(Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channel.setActive(false);
		channelRepo.save(channel);
		return MessageConstants.CHANNEL_DISABLED;
	}
	
	public List<Channels> getUserChannels(){
		User newUser=getUser();
		List<Channels> userChannels = channelRepo.findByUser(newUser);
		if(Objects.isNull(userChannels)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		
		return userChannels;
	}
	
	public boolean addUpdateTitle(UpdateTitleDTO updateTitleDTO) {
		Channels channel=channelRepo.findByChannelId(updateTitleDTO.getChannelId());
		if(Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		UpdateTitle updateTitle=new UpdateTitle();
		updateTitle.setChannel(channel);
		updateTitle.setTitle(updateTitleDTO.getTitle());
		updateTitle.setDescription(updateTitleDTO.getDescription());
		updateTitleRepo.save(updateTitle);
		return true;
	}
	
	public UpdateTitleDTO getUpdateTitle(ChannelStatus account) {
		Channels channel=channelRepo.findByChannelId(account.getChannelId());
		if(Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		UpdateTitle data = updateTitleRepo.findByChannel(channel);
		if(Objects.isNull(data)) {
			throw new AppException(MessageConstants.TITLE_NOT_FOUND);
		}
		UpdateTitleDTO updateTitleDTO=new UpdateTitleDTO();
		updateTitleDTO.setTitle(data.getTitle());
		updateTitleDTO.setDescription(data.getDescription());
		return updateTitleDTO;
	}
}
