package com.arraigntech.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		Channels channels=channelRepo.findByAccount(channelDTO.getAccount());
		if(Objects.nonNull(channels)) {
			throw new AppException(MessageConstants.CHANNEL_EXISTS);
		}
		channels=new Channels();
		User newUser=getUser();
		channels.setAccount(channelDTO.getAccount());
		channels.setChannelName(channelDTO.getChannelName());
		channels.setChannelUrl(channelDTO.getChannelUrl());
		channels.setActive(true);
		channels.setUser(newUser);
		channelRepo.save(channels);
		return true;		
	}
	
	public String enableChannel(ChannelStatus channelStatus) {
		Channels channel=channelRepo.findByAccount(channelStatus.getAccount());
		if(Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channel.setActive(true);
		channelRepo.save(channel);
		return MessageConstants.CHANNEL_ENABLED;
	}
	
	public String disableChannel(ChannelStatus channelStatus) {
		Channels channel=channelRepo.findByAccount(channelStatus.getAccount());
		if(Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channel.setActive(false);
		channelRepo.save(channel);
		return MessageConstants.CHANNEL_DISABLED;
	}
	
	public List<ChannelDTO> getUserChannels(){
		User newUser=getUser();
		List<Channels> userChannels = channelRepo.findByUser(newUser);
		List<ChannelDTO> channelList=new ArrayList<>();
		userChannels.forEach(channel->{
			ChannelDTO channelDTO=new ChannelDTO(channel.getAccount(),channel.getChannelName(),channel.getChannelUrl());
			channelList.add(channelDTO);
		});
		return channelList;
	}
	
	public boolean addUpdateTitle(UpdateTitleDTO updateTitleDTO) {
		Channels channel=channelRepo.findByAccount(updateTitleDTO.getAccount());
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
		Channels channel=channelRepo.findByAccount(account.getAccount());
		if(Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		UpdateTitle data = updateTitleRepo.findByChannel(channel);
		UpdateTitleDTO updateTitleDTO=new UpdateTitleDTO();
		updateTitleDTO.setTitle(data.getTitle());
		updateTitleDTO.setDescription(data.getDescription());
		return updateTitleDTO;
	}

}
