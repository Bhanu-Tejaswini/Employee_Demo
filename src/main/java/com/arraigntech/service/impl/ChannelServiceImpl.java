package com.arraigntech.service.impl;

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

import com.arraigntech.entity.Channels;
import com.arraigntech.entity.UpdateTitle;
import com.arraigntech.entity.User;
import com.arraigntech.exceptions.AppException;
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
import com.arraigntech.service.ChannelService;
import com.arraigntech.service.UserService;
import com.arraigntech.utility.ChannelTypeProvider;
import com.arraigntech.utility.MessageConstants;
import com.arraigntech.utility.RandomIdGenerator;

@Service
public class ChannelServiceImpl implements ChannelService {

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
	private UserService userService;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public boolean createChannel(ChannelDTO channelDTO) {
		if (log.isDebugEnabled()) {
			log.debug("createChannel method start {}.", channelDTO);
		}
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
		User newUser = userService.getUser();
		channels.setType(ChannelTypeProvider.YOUTUBE);
		channels.setChannelId(channelid);
		channels.setStreamName(ingestionInfo.getStreamName());
		channels.setPrimaryUrl(ingestionInfo.getIngestionAddress());
		channels.setBackupUrl(ingestionInfo.getBackupIngestionAddress());
		channels.setActive(true);
		channels.setUser(newUser);
		channelRepo.save(channels);
		log.debug("createChannel Method end");
		if (log.isDebugEnabled()) {
			log.debug("createChannel method end");
		}
		return true;
	}

	@Override
	public boolean addFaceBookChannel(ChannelDTO channelDTO) {
		if (log.isDebugEnabled()) {
			log.debug("addFaceBookChannel method start {}.", channelDTO);
		}
		User newUser = userService.getUser();
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
		if (log.isDebugEnabled()) {
			log.debug("addFaceBookChannel method end");
		}
		return true;
	}

	@Override
	public boolean addInstagramChannel(CustomChannelDTO customChannelDTO) {
		if (log.isDebugEnabled()) {
			log.debug("addInstagramChannel method start {}.", customChannelDTO);
		}
		if (Objects.isNull(customChannelDTO) || !StringUtils.hasText(customChannelDTO.getRtmpUrl())
				|| !StringUtils.hasText(customChannelDTO.getStreamKey())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if (!(customChannelDTO.getRtmpUrl().startsWith("rtmps://")
				&& customChannelDTO.getRtmpUrl().endsWith("/rtmp/"))) {
			throw new AppException(MessageConstants.WRONG_INSTAGRAM_RTMPSURL);
		}
		User newUser = userService.getUser();
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
		if (log.isDebugEnabled()) {
			log.debug("addInstagramChannel method end");
		}
		return true;
	}

	@Override
	public String execute(String accessToken) {
		if (log.isDebugEnabled()) {
			log.debug("execute method start {}", accessToken);
		}
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
		if (log.isDebugEnabled()) {
			log.debug("execute method end");
		}
		return response.getAccess_token();
	}

	@Override
	public boolean removechannel(String channelId) {
		if (log.isDebugEnabled()) {
			log.debug("removeChannel method start {}", channelId);
		}
		if (!StringUtils.hasText(channelId)) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		Channels newchannel = channelRepo.findByChannelId(channelId);
		if (Objects.isNull(newchannel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channelRepo.delete(newchannel);
		if (log.isDebugEnabled()) {
			log.debug("removeChannel method end");
		}
		return true;
	}

	@Override
	public String enableChannel(ChannelStatus channelStatus) {
		if (log.isDebugEnabled()) {
			log.debug("enableChannel method start {}.", channelStatus);
		}

		if (!StringUtils.hasText(channelStatus.getChannelId())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		Channels channel = channelRepo.findByChannelId(channelStatus.getChannelId());
		if (Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channel.setActive(true);
		channelRepo.save(channel);
		if (log.isDebugEnabled()) {
			log.debug("enableChannel method end");
		}
		return MessageConstants.CHANNEL_ENABLED;
	}

	@Override
	public String disableChannel(ChannelStatus channelStatus) {
		if (log.isDebugEnabled()) {
			log.debug("disableChannel method start {}.", channelStatus);
		}
		if (!StringUtils.hasText(channelStatus.getChannelId())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		Channels channel = channelRepo.findByChannelId(channelStatus.getChannelId());
		if (Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channel.setActive(false);
		channelRepo.save(channel);
		if (log.isDebugEnabled()) {
			log.debug("disableChannel method end");
		}
		return MessageConstants.CHANNEL_DISABLED;
	}

	@Override
	public List<Channels> findByUserAndTypeAndActive(User newUser, ChannelTypeProvider type, boolean active) {
		if (log.isDebugEnabled()) {
			log.debug("findByUserAndTypeAndActive method");
		}
		return channelRepo.findByUserAndTypeAndActive(newUser, type, active);
	}

	@Override
	public ChannelListUIResponse getUserChannels() {
		if (log.isDebugEnabled()) {
			log.debug("getUserChannels method start");
		}
		User newUser = userService.getUser();
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
		if (log.isDebugEnabled()) {
			log.debug("getUserChannels method end");
		}
		if (flag)
			return new ChannelListUIResponse(userChannels, true);
		else
			return new ChannelListUIResponse(userChannels, false);
	}

	/**
	 * @param channel
	 * @return
	 */
	@Override
	public boolean validChannel(Channels channel) {
		if (log.isDebugEnabled()) {
			log.debug("validChannel method start");
		}
		String url = "https://graph.facebook.com/" + channel.getFacebookUserId() + "?access_token="
				+ channel.getAccessToken();
		System.out.println(url);

		try {
			restTemplate.exchange(url, HttpMethod.POST, HttpEntity.EMPTY, String.class);
		} catch (Exception e) {
			return false;
		}
		if (log.isDebugEnabled()) {
			log.debug("validChannel method end");
		}
		return true;
	}

	@Override
	public List<ChannelErrorUIResponse> getErrorChannels() {
		if (log.isDebugEnabled()) {
			log.debug("getErrorChannels method start");
		}
		User newUser = userService.getUser();
		List<Channels> facebookChannels = channelRepo.findByUserAndType(newUser, ChannelTypeProvider.FACEBOOK);
		List<ChannelErrorUIResponse> channelList = new ArrayList<>();
		facebookChannels.stream().forEach(channel -> {

			if (!validChannel(channel)) {
				ChannelErrorUIResponse errorChannel = new ChannelErrorUIResponse(channel.getType(),
						channel.getChannelId());
				channelList.add(errorChannel);
			}
		});
		if (log.isDebugEnabled()) {
			log.debug("getErrorChannels method end");
		}
		return channelList;
	}

	@Override
	public boolean addUpdateTitle(UpdateTitleDTO updateTitleDTO) {
		if (log.isDebugEnabled()) {
			log.debug("addUpdateTitle method start");
		}
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
		if (log.isDebugEnabled()) {
			log.debug("addUpdateTitle method end");
		}
		return true;
	}

	@Override
	public UpdateTitleDTO getUpdateTitle(String channelId) {
		if (log.isDebugEnabled()) {
			log.debug("getUpdateTitle method start {}.", channelId);
		}
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
		if (log.isDebugEnabled()) {
			log.debug("getUpdateTitle method end");
		}
		return updateTitleDTO;
	}

	@Override
	public boolean updateAllTitles(UpdateAllTitleDTO updateAllTitleDTO) {
		if (log.isDebugEnabled()) {
			log.debug("updateAllTitles method start {}.", updateAllTitleDTO);
		}
		if (Objects.isNull(updateAllTitleDTO) || !StringUtils.hasText(updateAllTitleDTO.getDescription())
				|| !StringUtils.hasText(updateAllTitleDTO.getTitle())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		User newUser = userService.getUser();
		List<Channels> channelList = channelRepo.findByUser(newUser);
		if (channelList.isEmpty()) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channelList.forEach(channel -> {
			addUpdateTitle(new UpdateTitleDTO(channel.getChannelId(), updateAllTitleDTO.getTitle(),
					updateAllTitleDTO.getDescription()));
		});
		if (log.isDebugEnabled()) {
			log.debug("updateAllTitles method end");
		}
		return true;
	}
}
