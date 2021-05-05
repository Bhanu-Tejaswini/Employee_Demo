package com.arraigntech.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.arraigntech.Exception.AppException;
import com.arraigntech.entity.Channels;
import com.arraigntech.entity.UpdateTitle;
import com.arraigntech.entity.User;
import com.arraigntech.model.ChannelDTO;
import com.arraigntech.model.ChannelStatus;
import com.arraigntech.model.UpdateAllTitleDTO;
import com.arraigntech.model.UpdateTitleDTO;
import com.arraigntech.repository.ChannelsRepository;
import com.arraigntech.repository.UpdateTitleRepository;
import com.arraigntech.repository.UserRespository;
import com.arraigntech.utility.AuthenticationProvider;
import com.arraigntech.utility.CommonUtils;
import com.arraigntech.utility.MessageConstants;
import com.arraigntech.utility.RandomIdGenerator;

@Service
public class ChannelServiceImpl {
	
	private static final String CLIENT_ID = "client_id";
	private static final String APP_SECRET = "app_secret";
	private static final String GRAPH_API_URL = "https://graph.facebook.com/oauth/access_token";
	private static final String FB_EXCHANGE_TOKEN1 = "fb_exchange_token";
	private static final String FB_EXCHANGE_TOKEN = "fb_exchange_token";
	private static final String CLIENT_SECRET = "client_secret";
	private static final String GRANT_TYPE = "grant_type";
	
	@Value("{facebook.appId}")
	private String appId;
	
	@Value("{facebook.appSecret}")
	private String appSecret;
	
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
		if(channelDTO.getGraphDomain()!=null && channelDTO.getGraphDomain().startsWith("facebook")) {
			return addFaceBookChannel(channelDTO);
		}
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
		channels.setType(channelAccount);
		channels.setChannelId(channelDTO.getItems().get(0).getId());
		channels.setActive(true);
		channels.setUser(newUser);
		channelRepo.save(channels);
		return true;		
	}
	
	private boolean addFaceBookChannel(ChannelDTO channelDTO) {
		Channels channels = new Channels();
		User newUser=getUser();
		channels.setType(AuthenticationProvider.FACEBOOK);
		channels.setAccessToken(channelDTO.getAccessToken());
		channels.setChannelId(RandomIdGenerator.generate());
		channels.setActive(true);
		channels.setUser(newUser);
		channelRepo.save(channels);
		return true;		
		
	}

	 public void execute(String accessToken) {
		 UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		 builder.scheme("https")
		 			.host(GRAPH_API_URL)
		            .queryParam(CLIENT_ID, appId)
		            .queryParam(GRANT_TYPE, FB_EXCHANGE_TOKEN1)
		            .queryParam(CLIENT_SECRET, appSecret)
		            .queryParam(FB_EXCHANGE_TOKEN, accessToken)
		            .build();

		    String url = builder.toString();

		    Facebook facebook = new FacebookTemplate(accessToken);
		    ResponseEntity<String> exchange = facebook.restOperations()
		            .exchange(url, HttpMethod.GET, HttpEntity.EMPTY, String.class);
		    String response = exchange.getBody();
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
		if(!StringUtils.hasText(channelStatus.getChannelId())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		Channels channel=channelRepo.findByChannelId(channelStatus.getChannelId());
		if(Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channel.setActive(true);
		channelRepo.save(channel);
		return MessageConstants.CHANNEL_ENABLED;
	}
	
	public String disableChannel(ChannelStatus channelStatus) {
		if(!StringUtils.hasText(channelStatus.getChannelId())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
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
		if(!StringUtils.hasText(updateTitleDTO.getTitle()) || !StringUtils.hasText(updateTitleDTO.getChannelId())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		Channels channel=channelRepo.findByChannelId(updateTitleDTO.getChannelId());
		if(Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		UpdateTitle newTitle=updateTitleRepo.findByChannel(channel);
		if(Objects.isNull(newTitle)) {
		UpdateTitle updateTitle=new UpdateTitle();
		updateTitle.setChannel(channel);
		updateTitle.setTitle(updateTitleDTO.getTitle());
		updateTitle.setDescription(updateTitleDTO.getDescription());
		updateTitleRepo.save(updateTitle);
		}
		else {
			newTitle.setTitle(updateTitleDTO.getTitle());
			newTitle.setDescription(updateTitleDTO.getDescription());
			updateTitleRepo.save(newTitle);
		}
		return true;
	}
	
	public UpdateTitleDTO getUpdateTitle(ChannelStatus account) {
		if(!StringUtils.hasText(account.getChannelId())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
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
	
	public boolean updateAllTitles(UpdateAllTitleDTO updateAllTitleDTO) {
		User newUser=getUser();
		List<Channels> channelList = channelRepo.findByUser(newUser);
		channelList.forEach(channel->{
			addUpdateTitle(new UpdateTitleDTO(channel.getChannelId(),updateAllTitleDTO.getTitle(),updateAllTitleDTO.getDescription()));
		});
		return true;
	}
}
