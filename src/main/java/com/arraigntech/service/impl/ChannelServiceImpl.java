package com.arraigntech.service.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.arraigntech.Exception.AppException;
import com.arraigntech.entity.Channels;
import com.arraigntech.entity.UpdateTitle;
import com.arraigntech.entity.User;
import com.arraigntech.model.ChannelDTO;
import com.arraigntech.model.ChannelErrorUIResponse;
import com.arraigntech.model.ChannelIngestionInfo;
import com.arraigntech.model.ChannelListUIResponse;
import com.arraigntech.model.ChannelStatus;
import com.arraigntech.model.CustomChannelDTO;
import com.arraigntech.model.FacebookLongLivedTokenResponse;
import com.arraigntech.model.UpdateAllTitleDTO;
import com.arraigntech.model.UpdateTitleDTO;
import com.arraigntech.repository.ChannelsRepository;
import com.arraigntech.repository.UpdateTitleRepository;
import com.arraigntech.repository.UserRespository;
import com.arraigntech.utility.ChannelTypeProvider;
import com.arraigntech.utility.CommonUtils;
import com.arraigntech.utility.MessageConstants;
import com.arraigntech.utility.RandomIdGenerator;

@Service
public class ChannelServiceImpl {

	public static final Logger log = LoggerFactory.getLogger(ChannelServiceImpl.class);
	private static final String CLIENT_ID = "client_id";
//	private static final String APP_SECRET = "app_secret";
	private static final String GRAPH_API_URL = "graph.facebook.com/oauth/access_token";
	private static final String FB_EXCHANGE_TOKEN1 = "fb_exchange_token";
	private static final String FB_EXCHANGE_TOKEN = "fb_exchange_token";
	private static final String CLIENT_SECRET = "client_secret";
	private static final String GRANT_TYPE = "grant_type";
	private static final String FACEBOOK = "facebook";

	@Value("${facebook.appId}")
	private String appId;

	@Value("${facebook.appSecret}")
	private String appSecret;

	@Autowired
	private ChannelsRepository channelRepo;

	@Autowired
	private UpdateTitleRepository updateTitleRepo;

	@Autowired
	private UserRespository userRepo;

	@Autowired
	private RestTemplate restTemplate;

	private User getUser() {
		User user = userRepo.findByEmail(CommonUtils.getUser());
		if (Objects.isNull(user)) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		return user;
	}

	public boolean createChannel(ChannelDTO channelDTO) {
		log.debug("createChannel method start");
		if (Objects.isNull(channelDTO)) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if (channelDTO.getGraphDomain() != null && channelDTO.getGraphDomain().startsWith(FACEBOOK)) {
			return addFaceBookChannel(channelDTO);
		}
		String channelid;
		try {
			if (channelDTO.getItems().size() > 1) {
				channelid = channelDTO.getItems().get(1).getSnippet().getChannelId();
			} else {
				channelid = channelDTO.getItems().get(0).getSnippet().getChannelId();
			}
		} catch (IndexOutOfBoundsException e) {
			throw new AppException(MessageConstants.CHANNEL_NOT_EXISTS);
		}

		if (channelDTO.getItems().isEmpty() || !StringUtils.hasText(channelid)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		Channels channel = channelRepo.findByChannelId(channelid);
		if (Objects.nonNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_EXISTS);
		}

//		ChannelTypeProvider channelAccount = null;
//		if(channelDTO.getItems().get(1).getKind().startsWith("youtube")) {
//			channelAccount=ChannelTypeProvider.YOUTUBE;
//		}else if(channelDTO.getItems().get(0).getKind().startsWith("facebook")) {
//			channelAccount=ChannelTypeProvider.FACEBOOK;
//		}

		ChannelIngestionInfo ingestionInfo;
		try {
			if (channelDTO.getItems().size() > 1) {
				ingestionInfo = channelDTO.getItems().get(1).getCdn().getIngestionInfo();
			} else {
				ingestionInfo = channelDTO.getItems().get(0).getCdn().getIngestionInfo();
			}
		} catch (IndexOutOfBoundsException e) {
			throw new AppException(MessageConstants.CHANNEL_NOT_EXISTS);
		}

		Channels channels = new Channels();
		User newUser = getUser();
		channels.setType(ChannelTypeProvider.YOUTUBE);
		channels.setChannelId(channelid);
		channels.setStreamName(ingestionInfo.getStreamName());
		channels.setPrimaryUrl(ingestionInfo.getIngestionAddress());
		channels.setBackupUrl(ingestionInfo.getBackupIngestionAddress());
		channels.setActive(true);
		channels.setUser(newUser);
		channelRepo.save(channels);
		log.debug("createChannel Method end");
		return true;
	}

	public boolean addFaceBookChannel(ChannelDTO channelDTO) {
		log.debug("addFaceBookChannel method start");
		User newUser = getUser();
		List<Channels> facebookChannel = channelRepo.findByUserAndType(newUser, ChannelTypeProvider.FACEBOOK);
		if (facebookChannel.size() > 0) {
			throw new AppException(MessageConstants.FACEBOOK_CHANNEL_EXISTS);
		}
		Channels channels = new Channels();
		if (!StringUtils.hasText(channelDTO.getUserId())) {
			throw new AppException(MessageConstants.FACEBOOK_USERID_NOTFOUND);
		}
		Channels channel = channelRepo.findByFacebookUserId(channelDTO.getUserId());
		if (Objects.nonNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_EXISTS);
		}
		channels.setType(ChannelTypeProvider.FACEBOOK);
		channels.setFacebookUserId(channelDTO.getUserId());
		channels.setAccessToken(execute(channelDTO.getAccessToken()));
		channels.setChannelId(RandomIdGenerator.generate(10));
		channels.setActive(true);
		channels.setUser(newUser);
		channelRepo.save(channels);
		log.debug("addFaceBookChannel method end");
		return true;

	}

	public boolean addInstagramChannel(CustomChannelDTO customChannelDTO) {
		log.debug("addInstagramChannel method start");
		if (Objects.isNull(customChannelDTO)) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if(!(customChannelDTO.getRtmpUrl().startsWith("rtmps://") && customChannelDTO.getRtmpUrl().endsWith("/rtmp/"))){
			throw new AppException(MessageConstants.WRONG_INSTAGRAM_RTMPSURL);
		}
		User newUser = getUser();
		List<Channels> channelsList = channelRepo.findByUserAndType(newUser, ChannelTypeProvider.INSTAGRAM);
		Channels channel;
		if (channelsList.isEmpty()) {
			channel = new Channels();
			channel.setChannelId(RandomIdGenerator.generate(10));
			channel.setPrimaryUrl(customChannelDTO.getRtmpUrl());
			channel.setBackupUrl(customChannelDTO.getRtmpUrl());
			channel.setType(ChannelTypeProvider.INSTAGRAM);
			channel.setStreamName(customChannelDTO.getStreamKey());
			channel.setUser(newUser);
			channel.setActive(true);
		} else {
			channel = channelsList.get(0);
			channel.setPrimaryUrl(customChannelDTO.getRtmpUrl());
			channel.setBackupUrl(customChannelDTO.getRtmpUrl());
			channel.setType(ChannelTypeProvider.INSTAGRAM);
			channel.setStreamName(customChannelDTO.getStreamKey());
			channel.setActive(true);
		}
		channelRepo.save(channel);
		log.debug("addInstagramChannel method end");
		return true;
	}

	public String execute(String accessToken) {
		log.debug("execute method start");
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		String url = builder.scheme("https").host(GRAPH_API_URL).queryParam(CLIENT_ID, appId)
				.queryParam(CLIENT_SECRET, appSecret).queryParam(GRANT_TYPE, FB_EXCHANGE_TOKEN1)
				.queryParam(FB_EXCHANGE_TOKEN, accessToken).build().toUriString();

		FacebookLongLivedTokenResponse response;
		try {
			ResponseEntity<FacebookLongLivedTokenResponse> responseBody = restTemplate.exchange(url, HttpMethod.POST,
					HttpEntity.EMPTY, FacebookLongLivedTokenResponse.class);
			response = responseBody.getBody();
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
		log.debug("execute method end");
		return response.getAccess_token();
	}

	public boolean removechannel(String channelId) {
		log.debug("removeChannel method start");
		if (!StringUtils.hasText(channelId)) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		Channels newchannel = channelRepo.findByChannelId(channelId);
		if (Objects.isNull(newchannel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channelRepo.delete(newchannel);
		log.debug("removeChannel method end");
		return true;
	}

	public String enableChannel(ChannelStatus channelStatus) {
		log.debug("enableChannel method start");
		if (!StringUtils.hasText(channelStatus.getChannelId())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		Channels channel = channelRepo.findByChannelId(channelStatus.getChannelId());
		if (Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channel.setActive(true);
		channelRepo.save(channel);
		log.debug("enableChannel method end");
		return MessageConstants.CHANNEL_ENABLED;
	}

	public String disableChannel(ChannelStatus channelStatus) {
		log.debug("disableChannel method start");
		if (!StringUtils.hasText(channelStatus.getChannelId())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		Channels channel = channelRepo.findByChannelId(channelStatus.getChannelId());
		if (Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channel.setActive(false);
		channelRepo.save(channel);
		log.debug("disableChannel method end");
		return MessageConstants.CHANNEL_DISABLED;
	}

	public ChannelListUIResponse getUserChannels() {
		log.debug("getUserChannels method start");
		User newUser = getUser();
		List<Channels> userChannels = channelRepo.findByUser(newUser);
		List<Channels> userFaceBookChannels = channelRepo.findByUserAndType(newUser, ChannelTypeProvider.FACEBOOK);
		boolean flag = true;
		for (Channels channel : userFaceBookChannels) {
			if (!validChannel(channel)) {
				flag = flag & false;
			}
		}
		if (Objects.isNull(userChannels)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		log.debug("getUserChannels method end");
		if (flag)
			return new ChannelListUIResponse(userChannels, true);
		else
			return new ChannelListUIResponse(userChannels, false);
	}

	/**
	 * @param channel
	 * @return
	 */
	public boolean validChannel(Channels channel) {
		log.debug("validChannel method start");
		String url = "https://graph.facebook.com/" + channel.getFacebookUserId() + "?access_token="
				+ channel.getAccessToken();
		System.out.println(url);

		try {
			restTemplate.exchange(url, HttpMethod.POST, HttpEntity.EMPTY, String.class);
		} catch (Exception e) {
			return false;
		}
		log.debug("validChannel method end");
		return true;
	}

	public List<ChannelErrorUIResponse> getErrorChannels() {
		log.debug("getErrorChannels method start");
		User newUser = getUser();
		List<Channels> facebookChannels = channelRepo.findByUserAndType(newUser, ChannelTypeProvider.FACEBOOK);
		List<ChannelErrorUIResponse> channelList = new ArrayList<>();
		facebookChannels.stream().forEach(channel -> {

			if (!validChannel(channel)) {
				ChannelErrorUIResponse errorChannel = new ChannelErrorUIResponse(channel.getType(),
						channel.getChannelId());
				channelList.add(errorChannel);
			}
		});
		log.debug("getErrorChannels method end");
		return channelList;
	}

	public boolean addUpdateTitle(UpdateTitleDTO updateTitleDTO) {
		log.debug("addUpdateTitle method start");
		if (!StringUtils.hasText(updateTitleDTO.getTitle()) || !StringUtils.hasText(updateTitleDTO.getChannelId())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		Channels channel = channelRepo.findByChannelId(updateTitleDTO.getChannelId());
		if (Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		UpdateTitle newTitle = updateTitleRepo.findByChannel(channel);
		if (Objects.isNull(newTitle)) {
			UpdateTitle updateTitle = new UpdateTitle();
			updateTitle.setChannel(channel);
			updateTitle.setTitle(updateTitleDTO.getTitle());
			updateTitle.setDescription(updateTitleDTO.getDescription());
			updateTitleRepo.save(updateTitle);
		} else {
			newTitle.setTitle(updateTitleDTO.getTitle());
			newTitle.setDescription(updateTitleDTO.getDescription());
			updateTitleRepo.save(newTitle);
		}
		log.debug("addUpdateTitle method end");
		return true;
	}

	public UpdateTitleDTO getUpdateTitle(String channelId) {
		log.debug("getUpdateTitle method start");
		if (!StringUtils.hasText(channelId)) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		Channels channel = channelRepo.findByChannelId(channelId);
		if (Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		UpdateTitle data = updateTitleRepo.findByChannel(channel);
		if (Objects.isNull(data)) {
			return new UpdateTitleDTO();
		}
		UpdateTitleDTO updateTitleDTO = new UpdateTitleDTO();
		updateTitleDTO.setTitle(data.getTitle());
		updateTitleDTO.setDescription(data.getDescription());
		updateTitleDTO.setChannelId(channelId);
		log.debug("getUpdateTitle method end");
		return updateTitleDTO;
	}

	public boolean updateAllTitles(UpdateAllTitleDTO updateAllTitleDTO) {
		log.debug("updateAllTitles method start");
		User newUser = getUser();
		List<Channels> channelList = channelRepo.findByUser(newUser);
		if (channelList.isEmpty()) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channelList.forEach(channel -> {
			addUpdateTitle(new UpdateTitleDTO(channel.getChannelId(), updateAllTitleDTO.getTitle(),
					updateAllTitleDTO.getDescription()));
		});
		log.debug("updateAllTitles method end");
		return true;
	}
}
