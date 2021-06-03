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

import com.arraigntech.entity.ChannelEntity;
import com.arraigntech.entity.UpdateTitleEntity;
import com.arraigntech.entity.UserEntity;
import com.arraigntech.exceptions.AppException;
import com.arraigntech.repository.ChannelsRepository;
import com.arraigntech.repository.UpdateTitleRepository;
import com.arraigntech.request.vo.ChannelIngestionInfoVO;
import com.arraigntech.request.vo.ChannelStatusVO;
import com.arraigntech.request.vo.ChannelVO;
import com.arraigntech.request.vo.CustomChannelVO;
import com.arraigntech.request.vo.UpdateAllTitleVO;
import com.arraigntech.response.vo.ChannelErrorUIResponseVO;
import com.arraigntech.response.vo.ChannelListUIResponseVO;
import com.arraigntech.response.vo.ChannelUIResponseVO;
import com.arraigntech.response.vo.FacebookLongLivedTokenResponseVO;
import com.arraigntech.response.vo.UpdateTitleVO;
import com.arraigntech.service.ChannelService;
import com.arraigntech.service.UserService;
import com.arraigntech.utility.ChannelTypeProvider;
import com.arraigntech.utility.MessageConstants;
import com.arraigntech.utility.RandomIdGenerator;

@Service
public class ChannelServiceImpl implements ChannelService {

	private static final String CLIENT_ID = "client_id";
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
	public boolean createChannel(ChannelVO channelDTO) {
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
		ChannelEntity channel = channelRepo.findByChannelId(channelid);
		if (Objects.nonNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_EXISTS);
		}

		ChannelIngestionInfoVO ingestionInfo;
		try {
			if (channelDTO.getItems().size() > 1) {
				ingestionInfo = channelDTO.getItems().get(1).getCdn().getIngestionInfo();
			} else {
				ingestionInfo = channelDTO.getItems().get(0).getCdn().getIngestionInfo();
			}
		} catch (IndexOutOfBoundsException e) {
			throw new AppException(MessageConstants.CHANNEL_NOT_EXISTS);
		}

		ChannelEntity channels = new ChannelEntity();
		UserEntity newUser = userService.getUser();
		channels.setType(ChannelTypeProvider.YOUTUBE);
		channels.setChannelId(channelid);
		channels.setStreamName(ingestionInfo.getStreamName());
		channels.setPrimaryUrl(ingestionInfo.getIngestionAddress());
		channels.setBackupUrl(ingestionInfo.getBackupIngestionAddress());
		channels.setActive(true);
		channels.setUser(newUser);
		channelRepo.save(channels);
		return true;
	}

	@Override
	public boolean addFaceBookChannel(ChannelVO channelDTO) {
		UserEntity newUser = userService.getUser();
		List<ChannelEntity> facebookChannel = channelRepo.findByUserAndType(newUser, ChannelTypeProvider.FACEBOOK);
		if (facebookChannel.size() > 0) {
			throw new AppException(MessageConstants.FACEBOOK_CHANNEL_EXISTS);
		}
		ChannelEntity channels = new ChannelEntity();
		if (!StringUtils.hasText(channelDTO.getUserId())) {
			throw new AppException(MessageConstants.FACEBOOK_USERID_NOTFOUND);
		}
		ChannelEntity channel = channelRepo.findByFacebookUserId(channelDTO.getUserId());
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
		return true;
	}

	@Override
	public boolean addInstagramChannel(CustomChannelVO customChannelDTO) {
		if (Objects.isNull(customChannelDTO) || !StringUtils.hasText(customChannelDTO.getRtmpUrl())
				|| !StringUtils.hasText(customChannelDTO.getStreamKey())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if (!(customChannelDTO.getRtmpUrl().startsWith("rtmps://")
				&& customChannelDTO.getRtmpUrl().endsWith("/rtmp/"))) {
			throw new AppException(MessageConstants.WRONG_INSTAGRAM_RTMPSURL);
		}
		UserEntity newUser = userService.getUser();
		List<ChannelEntity> channelsList = channelRepo.findByUserAndType(newUser, ChannelTypeProvider.INSTAGRAM);
		ChannelEntity channel;
		if (channelsList.isEmpty()) {
			channel = new ChannelEntity();
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
		return true;
	}

	@Override
	public String execute(String accessToken) {
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		String url = builder.scheme("https").host(GRAPH_API_URL).queryParam(CLIENT_ID, appId)
				.queryParam(CLIENT_SECRET, appSecret).queryParam(GRANT_TYPE, FB_EXCHANGE_TOKEN1)
				.queryParam(FB_EXCHANGE_TOKEN, accessToken).build().toUriString();

		FacebookLongLivedTokenResponseVO response;
		try {
			ResponseEntity<FacebookLongLivedTokenResponseVO> responseBody = restTemplate.exchange(url, HttpMethod.POST,
					HttpEntity.EMPTY, FacebookLongLivedTokenResponseVO.class);
			response = responseBody.getBody();
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
		return response.getAccess_token();
	}

	@Override
	public boolean removechannel(String channelId) {
		if (!StringUtils.hasText(channelId)) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		ChannelEntity newchannel = channelRepo.findByChannelId(channelId);
		if (Objects.isNull(newchannel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channelRepo.delete(newchannel);
		return true;
	}

	@Override
	public String enableChannel(ChannelStatusVO channelStatus) {
		if (!StringUtils.hasText(channelStatus.getChannelId())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		ChannelEntity channel = channelRepo.findByChannelId(channelStatus.getChannelId());
		if (Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channel.setActive(true);
		channelRepo.save(channel);
		return MessageConstants.CHANNEL_ENABLED;
	}

	@Override
	public String disableChannel(ChannelStatusVO channelStatus) {
		if (!StringUtils.hasText(channelStatus.getChannelId())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		ChannelEntity channel = channelRepo.findByChannelId(channelStatus.getChannelId());
		if (Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channel.setActive(false);
		channelRepo.save(channel);
		return MessageConstants.CHANNEL_DISABLED;
	}

	@Override
	public List<ChannelEntity> findByUserAndTypeAndActive(UserEntity newUser, ChannelTypeProvider type, boolean active) {
		return channelRepo.findByUserAndTypeAndActive(newUser, type, active);
	}

	@Override
	public ChannelListUIResponseVO getUserChannels() {
		UserEntity newUser = userService.getUser();
		List<ChannelEntity> userChannels = channelRepo.findByUser(newUser);
		List<ChannelEntity> userFaceBookChannels = channelRepo.findByUserAndType(newUser, ChannelTypeProvider.FACEBOOK);
		boolean flag = true;
		for (ChannelEntity channel : userFaceBookChannels) {
			if (!validChannel(channel)) {
				flag = flag & false;
			}
		}
		if (Objects.isNull(userChannels)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		List<ChannelUIResponseVO> channelsList = new ArrayList<>();
		userChannels.stream().forEach(channel -> {
			channelsList.add(new ChannelUIResponseVO(newUser.getUsername(), channel.getType(), channel.getChannelId(),
					channel.getStreamName(), channel.getPrimaryUrl(), channel.getBackupUrl(), channel.isActive(),
					channel.getAccessToken(), channel.getFacebookUserId(), channel.getUpdateTitle()));
		});
		if (flag)
			return new ChannelListUIResponseVO(channelsList, true);
		else
			return new ChannelListUIResponseVO(channelsList, false);
	}

	/**
	 * @param channel
	 * @return
	 */
	@Override
	public boolean validChannel(ChannelEntity channel) {
		String url = "https://graph.facebook.com/" + channel.getFacebookUserId() + "?access_token="
				+ channel.getAccessToken();
		System.out.println(url);

		try {
			restTemplate.exchange(url, HttpMethod.POST, HttpEntity.EMPTY, String.class);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public List<ChannelErrorUIResponseVO> getErrorChannels() {
		UserEntity newUser = userService.getUser();
		List<ChannelEntity> facebookChannels = channelRepo.findByUserAndType(newUser, ChannelTypeProvider.FACEBOOK);
		List<ChannelErrorUIResponseVO> channelList = new ArrayList<>();
		facebookChannels.stream().forEach(channel -> {

			if (!validChannel(channel)) {
				ChannelErrorUIResponseVO errorChannel = new ChannelErrorUIResponseVO(channel.getType(),
						channel.getChannelId());
				channelList.add(errorChannel);
			}
		});
		return channelList;
	}

	@Override
	public boolean addUpdateTitle(UpdateTitleVO updateTitleDTO) {
		if (!StringUtils.hasText(updateTitleDTO.getTitle()) || !StringUtils.hasText(updateTitleDTO.getChannelId())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		ChannelEntity channel = channelRepo.findByChannelId(updateTitleDTO.getChannelId());
		if (Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		UpdateTitleEntity newTitle = updateTitleRepo.findByChannel(channel);
		if (Objects.isNull(newTitle)) {
			UpdateTitleEntity updateTitle = new UpdateTitleEntity();
			updateTitle.setChannel(channel);
			updateTitle.setTitle(updateTitleDTO.getTitle());
			updateTitle.setDescription(updateTitleDTO.getDescription());
			updateTitleRepo.save(updateTitle);
		} else {
			newTitle.setTitle(updateTitleDTO.getTitle());
			newTitle.setDescription(updateTitleDTO.getDescription());
			updateTitleRepo.save(newTitle);
		}
		return true;
	}

	@Override
	public UpdateTitleVO getUpdateTitle(String channelId) {
		if (!StringUtils.hasText(channelId)) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		ChannelEntity channel = channelRepo.findByChannelId(channelId);
		if (Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		UpdateTitleEntity data = updateTitleRepo.findByChannel(channel);
		if (Objects.isNull(data)) {
			return new UpdateTitleVO();
		}
		UpdateTitleVO updateTitleDTO = new UpdateTitleVO();
		updateTitleDTO.setTitle(data.getTitle());
		updateTitleDTO.setDescription(data.getDescription());
		updateTitleDTO.setChannelId(channelId);
		return updateTitleDTO;
	}

	@Override
	public boolean updateAllTitles(UpdateAllTitleVO updateAllTitleDTO) {
		if (Objects.isNull(updateAllTitleDTO) || !StringUtils.hasText(updateAllTitleDTO.getTitle())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		UserEntity newUser = userService.getUser();
		List<ChannelEntity> channelList = channelRepo.findByUser(newUser);
		if (channelList.isEmpty()) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channelList.forEach(channel -> {
			addUpdateTitle(new UpdateTitleVO(channel.getChannelId(), updateAllTitleDTO.getTitle(),
					updateAllTitleDTO.getDescription()));
		});
		return true;
	}
}
