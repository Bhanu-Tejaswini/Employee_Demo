package com.arraigntech.service.impl;

import java.util.List;
import java.util.Objects;

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
import com.arraigntech.model.ChannelIngestionInfo;
import com.arraigntech.model.ChannelStatus;
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

	private static final String CLIENT_ID = "client_id";
	private static final String APP_SECRET = "app_secret";
	private static final String GRAPH_API_URL = "graph.facebook.com/oauth/access_token";
	private static final String FB_EXCHANGE_TOKEN1 = "fb_exchange_token";
	private static final String FB_EXCHANGE_TOKEN = "fb_exchange_token";
	private static final String CLIENT_SECRET = "client_secret";
	private static final String GRANT_TYPE = "grant_type";

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
		if (channelDTO.getGraphDomain() != null && channelDTO.getGraphDomain().startsWith("facebook")) {
			return addFaceBookChannel(channelDTO);
		}
		String channelid;
		System.out.println(channelDTO.getItems().size());
		if (channelDTO.getItems().size() > 1) {
			channelid = channelDTO.getItems().get(1).getSnippet().getChannelId();
		} else {
			channelid = channelDTO.getItems().get(0).getSnippet().getChannelId();
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
		if (channelDTO.getItems().size() > 1) {
			ingestionInfo = channelDTO.getItems().get(1).getCdn().getIngestionInfo();
		} else {
			ingestionInfo = channelDTO.getItems().get(0).getCdn().getIngestionInfo();
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
		return true;
	}

	private boolean addFaceBookChannel(ChannelDTO channelDTO) {
		Channels channels = new Channels();
		User newUser = getUser();
		channels.setType(ChannelTypeProvider.FACEBOOK);
		channels.setFacebookUserId(channelDTO.getUserId());
		channels.setAccessToken(execute(channelDTO.getAccessToken()));
		channels.setChannelId(RandomIdGenerator.generate());
		channels.setActive(true);
		channels.setUser(newUser);
		channelRepo.save(channels);
		return true;

	}

	public String execute(String accessToken) {
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
		return response.getAccess_token();
	}

	public boolean removechannel(String channelId) {
		if (!StringUtils.hasText(channelId)) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		Channels newchannel = channelRepo.findByChannelId(channelId);
		if (Objects.isNull(newchannel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channelRepo.delete(newchannel);
		return true;
	}

	public String enableChannel(ChannelStatus channelStatus) {
		if (!StringUtils.hasText(channelStatus.getChannelId())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		Channels channel = channelRepo.findByChannelId(channelStatus.getChannelId());
		if (Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channel.setActive(true);
		channelRepo.save(channel);
		return MessageConstants.CHANNEL_ENABLED;
	}

	public String disableChannel(ChannelStatus channelStatus) {
		if (!StringUtils.hasText(channelStatus.getChannelId())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		Channels channel = channelRepo.findByChannelId(channelStatus.getChannelId());
		if (Objects.isNull(channel)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}
		channel.setActive(false);
		channelRepo.save(channel);
		return MessageConstants.CHANNEL_DISABLED;
	}

	public List<Channels> getUserChannels() {
		User newUser = getUser();
		List<Channels> userChannels = channelRepo.findByUser(newUser);
		if (Objects.isNull(userChannels)) {
			throw new AppException(MessageConstants.CHANNEL_NOT_FOUND);
		}

		return userChannels;
	}

	public boolean addUpdateTitle(UpdateTitleDTO updateTitleDTO) {
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
		return true;
	}

	public UpdateTitleDTO getUpdateTitle(String channelId) {
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
		return updateTitleDTO;
	}

	public boolean updateAllTitles(UpdateAllTitleDTO updateAllTitleDTO) {
		User newUser = getUser();
		List<Channels> channelList = channelRepo.findByUser(newUser);
		channelList.forEach(channel -> {
			addUpdateTitle(new UpdateTitleDTO(channel.getChannelId(), updateAllTitleDTO.getTitle(),
					updateAllTitleDTO.getDescription()));
		});
		return true;
	}
}
