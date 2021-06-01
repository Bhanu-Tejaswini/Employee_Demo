package com.arraigntech.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.arraigntech.entity.Channels;
import com.arraigntech.entity.StreamTarget;
import com.arraigntech.entity.Streams;
import com.arraigntech.entity.User;
import com.arraigntech.exceptions.AppException;
import com.arraigntech.model.MongoUser;
import com.arraigntech.mongorepos.MongoStreamResponseRepository;
import com.arraigntech.mongorepos.MongoStreamTargetRepository;
import com.arraigntech.mongorepos.OutputStreamTargetRepository;
import com.arraigntech.repository.StreamRepository;
import com.arraigntech.repository.StreamTargetRepository;
import com.arraigntech.service.ChannelService;
import com.arraigntech.service.IVSStreamService;
import com.arraigntech.service.UserService;
import com.arraigntech.streams.model.FacebookStreamRequest;
import com.arraigntech.streams.model.FacebookStreamResponse;
import com.arraigntech.streams.model.FetchStreamUIResponse;
import com.arraigntech.streams.model.IVSLiveStream;
import com.arraigntech.streams.model.IVSLiveStreamResponse;
import com.arraigntech.streams.model.LiveStream;
import com.arraigntech.streams.model.LiveStreamState;
import com.arraigntech.streams.model.OutputStreamTarget;
import com.arraigntech.streams.model.OutputStreamTargetDTO;
import com.arraigntech.streams.model.StreamSourceConnectionInformation;
import com.arraigntech.streams.model.StreamTargetDTO;
import com.arraigntech.streams.model.StreamTargetModel;
import com.arraigntech.streams.model.StreamUIRequest;
import com.arraigntech.streams.model.StreamUIResponse;
import com.arraigntech.streams.model.Webrtc;
import com.arraigntech.utility.ChannelTypeProvider;
import com.arraigntech.utility.MessageConstants;
import com.arraigntech.utility.RandomIdGenerator;

/**
 * The Class IVSStreamServiceImpl.
 *
 * @author Bhaskara S
 */
@Service
public class IVSStreamServiceImpl implements IVSStreamService {

	/** The Constant log. */
	public static final Logger log = LoggerFactory.getLogger(IVSStreamServiceImpl.class);
	private static final String BILLINT_MODE = "pay_as_you_go";
	private static final String BROADCAST_LOCATION = "asia_pacific_india";
	private static final String ENCODER = "other_webrtc";
	private static final String TRANSCODER_TYPE = "transcoded";
	private static final String DELIVERY_METHOD = "push";
	private static final String DELIVERY_TYPE = "single-bitrate";
	private static final boolean PLAYER_RESPONSIVE = true;
	private static final boolean RECORDING = false;
	private static final String STATUS = "status";
	private static final String LIVE_NOW = "LIVE_NOW";
	private static final String ACCESS_TOKEN = "access_token";
	public static final String RTMPS = "rtmps";
	public static final String RTMP = "rtmp";
	private static final String STOPPED = "stopped";

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private StreamRepository streamRepo;

	@Value("${wowza.url}")
	private String baseUrl;

	@Value("${wsc-api-key}")
	private String wscApiKey;

	@Value("${wsc-access-key}")
	private String wscAccessKey;

	@Value("${facebook.streamurl}")
	private String graphUrl;

	@Autowired
	private UserService userService;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private MongoStreamResponseRepository streamResponseRepo;

	@Autowired
	private MongoStreamTargetRepository mongoStreamTargetRepo;

	@Autowired
	private OutputStreamTargetRepository outputStreamTargetRepo;

	@Autowired
	private StreamTargetRepository streamTargetRepo;

	/**
	 * Gets the header.
	 *
	 * @return the header
	 */
	public MultiValueMap<String, String> getHeader() {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Content-Type", "application/json");
		headers.set("wsc-api-key", wscApiKey);
		headers.set("wsc-access-key", wscAccessKey);
		if (log.isDebugEnabled()) {
			log.debug("wowza header {}.", headers);
		}
		return headers;
	}

	/**
	 * Creates the stream.
	 *
	 * @param streamRequest the stream request
	 * @return the stream UI response
	 */
	@Override
	public StreamUIResponse createStream(StreamUIRequest streamRequest) {
		if (log.isDebugEnabled()) {
			log.debug("createStream method start {}.", streamRequest);
		}
		User newUser = userService.getUser();
		IVSLiveStream liveStream = populateStreamData(streamRequest);
		String url = baseUrl + "/live_streams";
		MultiValueMap<String, String> headers = getHeader();
		HttpEntity<IVSLiveStream> request = new HttpEntity<>(liveStream, headers);
		IVSLiveStreamResponse liveStreamResponse;
		try {
			ResponseEntity<IVSLiveStreamResponse> reponseEntity = restTemplate.exchange(url, HttpMethod.POST, request,
					IVSLiveStreamResponse.class);
			liveStreamResponse = reponseEntity.getBody();
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.error(e.getMessage());
			}
			throw new AppException(e.getMessage());
		}

		// saving the response data to mongodb
		liveStreamResponse.setUser(new MongoUser(newUser.getId(), newUser.getEmail(), newUser.getUsername()));
		streamResponseRepo.save(liveStreamResponse);
		// saving necessary details to postgresql
		saveStream(liveStreamResponse);

		// checking whether stream has been started completly, if started return the
		// response
		String streamId = liveStreamResponse.getLiveStreamResponse().getId();
		String outputId = liveStreamResponse.getLiveStreamResponse().getDirect_playback_urls().getWebrtc().get(2)
				.getOutput_id();
		// adds youtube channels in the target
		channelsStream(streamId, outputId);
		CompletableFuture.runAsync(() -> startStream(streamId));
		CompletableFuture.runAsync(() -> deleteOutputTargetAll(
				liveStreamResponse.getLiveStreamResponse().getDirect_playback_urls().getWebrtc(), streamId));
		StreamSourceConnectionInformation response = liveStreamResponse.getLiveStreamResponse()
				.getSource_connection_information();
		if (log.isDebugEnabled()) {
			log.debug("creatStream method end");
		}
		return new StreamUIResponse(response.getSdp_url(), response.getApplication_name(), response.getStream_name(),
				streamId, "starting");
	}

	/**
	 * @param streamRequest
	 * @return
	 */
	private IVSLiveStream populateStreamData(StreamUIRequest streamRequest) {
		if (log.isDebugEnabled()) {
			log.debug("populateStreamData method start {}.", streamRequest);
		}
		IVSLiveStream ivsLiveStream = new IVSLiveStream();
		LiveStream liveStream = new LiveStream();
		liveStream.setAspect_ratio_height(streamRequest.getAspectRatioHeight());
		liveStream.setAspect_ratio_width(streamRequest.getAspectRatioWidth());
		liveStream.setBilling_mode(BILLINT_MODE);
		liveStream.setBroadcast_location(BROADCAST_LOCATION);
		liveStream.setDelivery_method(DELIVERY_METHOD);
		liveStream.setDelivery_type(DELIVERY_TYPE);
		liveStream.setEncoder(ENCODER);
		liveStream.setName("STREAM_" + RandomIdGenerator.generate(10));
		liveStream.setPlayer_responsive(PLAYER_RESPONSIVE);
		liveStream.setRecording(RECORDING);
		liveStream.setTranscoder_type(TRANSCODER_TYPE);
		ivsLiveStream.setLiveStream(liveStream);
		if (log.isDebugEnabled()) {
			log.debug("populateStreamData method end");
		}
		return ivsLiveStream;
	}

	/**
	 * Delete stream.
	 *
	 * @param streamId the stream id
	 * @return true, if successful
	 */
	@Override
	public boolean deleteStream(String streamId) {
		if (log.isDebugEnabled()) {
			log.debug("deleteStream method start {}", streamId);
		}
		Streams stream = streamRepo.findByStreamId(streamId);
		if (Objects.isNull(stream)) {
			if (log.isDebugEnabled()) {
				log.error(MessageConstants.STREAM_NOT_EXISTS);
			}
			throw new AppException(MessageConstants.STREAM_NOT_EXISTS);
		}
		List<StreamTarget> streamTargets = streamTargetRepo.findByStream(stream);
		if (stream.isActive()) {
			if (!fetchStreamState(streamId).equals(STOPPED))
				stopStream(streamId);
		}
		String url = baseUrl + "/live_streams/" + streamId;
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		try {
			restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.error(e.getMessage());
			}
			throw new AppException(e.getMessage());
		}
		if (streamTargets.isEmpty()) {
			return true;
		}
		// Also deleting the stream target
		streamTargets.stream().forEach(streamTarget -> deleteStreamTarget(streamTarget.getStreamTargetId()));
		if (log.isDebugEnabled()) {
			log.debug("deleteStream method end");
		}
		return true;
	}

	public Boolean channelsStream(String streamId, String outputId) {
		if (log.isDebugEnabled()) {
			log.debug("channelStream method start");
		}
		User newUser = userService.getUser();
		List<Channels> youtubeChannels = channelService.findByUserAndTypeAndActive(newUser, ChannelTypeProvider.YOUTUBE,
				true);
		List<Channels> facebookChannels = channelService.findByUserAndTypeAndActive(newUser,
				ChannelTypeProvider.FACEBOOK, true);
		List<Channels> instagramChannels = channelService.findByUserAndTypeAndActive(newUser,
				ChannelTypeProvider.INSTAGRAM, true);
		// if there are no channels added means
		if (youtubeChannels.isEmpty() && facebookChannels.isEmpty()) {
			deleteStream(streamId);
			if (log.isDebugEnabled()) {
				log.error(MessageConstants.NO_CHANNELS_TO_STREAM);
			}
			throw new AppException(MessageConstants.NO_CHANNELS_TO_STREAM);
		}
		CompletableFuture.runAsync(() -> youtubeStream(youtubeChannels, streamId, outputId));
		CompletableFuture.runAsync(() -> facebookStream(facebookChannels, streamId, outputId)).handle((res, e) -> {
			throw new AppException(e.getMessage());
		});
		CompletableFuture.runAsync(() -> instagramStream(instagramChannels, streamId, outputId));
		if (log.isDebugEnabled()) {
			log.debug("channelStream method end");
		}
		return true;
	}

	/**
	 * @param instagramChannels
	 * @param streamId
	 * @param outputId
	 * @return
	 */
	public void instagramStream(List<Channels> instagramChannels, String streamId, String outputId) {
		if (log.isDebugEnabled()) {
			log.debug("instagramStream method start");
		}
		instagramChannels.stream().forEach(channel -> {
			String streamTargetId = createStreamTarget(
					new StreamTargetModel("INSTGRAM_" + RandomIdGenerator.generate(5), RTMPS, channel.getPrimaryUrl(),
							channel.getStreamName(), channel.getBackupUrl()),
					streamId);
			CompletableFuture.runAsync(() -> addStreamTarget(streamId, outputId, streamTargetId));
		});
		if (log.isDebugEnabled()) {
			log.debug("instagramStream method end");
		}
	}

	/**
	 * @param facebookChannels
	 * @param streamId
	 * @param outputId
	 */
	private void facebookStream(List<Channels> facebookChannels, String streamId, String outputId) {
		if (log.isDebugEnabled()) {
			log.debug("facebookStream method start");
		}
		facebookChannels.stream().forEach(channel -> {
			FacebookStreamRequest facebookStreamRequest = getFacebookStreamData(channel);
			String streamTargetId = createStreamTarget(new StreamTargetModel(
					"FACEBOOK_" + RandomIdGenerator.generate(5), RTMPS, facebookStreamRequest.getPrimaryUrl(),
					facebookStreamRequest.getStreamName(), facebookStreamRequest.getPrimaryUrl()), streamId);
			CompletableFuture.runAsync(() -> addStreamTarget(streamId, outputId, streamTargetId));
		});
		if (log.isDebugEnabled()) {
			log.debug("facebookStream method end");
		}
	}

	/**
	 * @return
	 */
	private FacebookStreamRequest getFacebookStreamData(Channels channel) {
		if (log.isDebugEnabled()) {
			log.debug("getFacebookStreamData method start {}.", channel);
		}
		log.debug("getFacebookStreamData start");
		String uri = graphUrl + channel.getFacebookUserId() + "/live_videos";
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		String url = builder.scheme("https").host(uri).queryParam(STATUS, LIVE_NOW)
				.queryParam(ACCESS_TOKEN, channel.getAccessToken()).build().toString();

		FacebookStreamResponse facebookStreamResponse;
		try {
			ResponseEntity<FacebookStreamResponse> responseBody = restTemplate.exchange(url, HttpMethod.POST,
					HttpEntity.EMPTY, FacebookStreamResponse.class);
			facebookStreamResponse = responseBody.getBody();
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.error(e.getMessage());
			}
			throw new AppException(e.getMessage());

		}
		String[] responseData = facebookStreamResponse.getStream_url().split("/");
		FacebookStreamRequest facebookStreamRequest = new FacebookStreamRequest();
		facebookStreamRequest.setPrimaryUrl(responseData[0] + "//" + responseData[2] + "/" + responseData[3] + "/");
		facebookStreamRequest.setStreamName(responseData[4]);
		if (log.isDebugEnabled()) {
			log.debug("getFacebookStreamData method end");
		}
		return facebookStreamRequest;
	}

	/**
	 * Youtube stream.
	 * 
	 * @param youtubeChannels
	 *
	 * @param streamId        the stream id
	 * @param outputId        the output id
	 * @return true, if successful
	 */
	@Override
	public void youtubeStream(List<Channels> youtubeChannels, String streamId, String outputId) {
		if (log.isDebugEnabled()) {
			log.debug("youtubestream method start");
		}
		youtubeChannels.stream().forEach(channel -> {
			String streamTargetId = createStreamTarget(new StreamTargetModel("YOUTUBE_" + RandomIdGenerator.generate(5),
					RTMP, channel.getPrimaryUrl(), channel.getStreamName(), channel.getBackupUrl()), streamId);
			CompletableFuture.runAsync(() -> addStreamTarget(streamId, outputId, streamTargetId));
		});
		if (log.isDebugEnabled()) {
			log.debug("youtubestream method end");
		}
	}

	/**
	 * Start stream.
	 *
	 * @param id the id
	 * @return the string
	 */
	@Override
	public void startStream(String id) {
		if (log.isDebugEnabled()) {
			log.debug("startStream method start {}", id);
		}
		String url = baseUrl + "/live_streams/" + id + "/start";
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		LiveStreamState response;
		try {
			ResponseEntity<LiveStreamState> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request,
					LiveStreamState.class);
			response = responseEntity.getBody();
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.error(e.getMessage());
			}
			throw new AppException(e.getMessage());
		}
		Streams stream = streamRepo.findByStreamId(id);
		stream.setActive(true);
		streamRepo.save(stream);
		if (log.isDebugEnabled()) {
			log.debug("startStream method end");
		}
	}

	/**
	 * Stop stream.
	 *
	 * @param id the id
	 * @return the string
	 */
	@Override
	public String stopStream(String id) {
		if (log.isDebugEnabled()) {
			log.debug("stopStream method start {}", id);
		}
		String url = baseUrl + "/live_streams/" + id + "/stop";
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		LiveStreamState response;
		try {
			ResponseEntity<LiveStreamState> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request,
					LiveStreamState.class);
			response = responseEntity.getBody();
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.error(e.getMessage());
			}
			throw new AppException(e.getMessage());
		}
		// updating the status of the stream
		Streams stream = streamRepo.findByStreamId(id);
		stream.setActive(false);
		streamRepo.save(stream);
		if (log.isDebugEnabled()) {
			log.debug("stopStream method end");
		}
		return response.getLiveStreamState().getState();
	}

	/**
	 * Fetch stream state.
	 *
	 * @param id the id
	 * @return the live stream state
	 */
	@Override
	public FetchStreamUIResponse fetchStreamState(String id) {
		if (log.isDebugEnabled()) {
			log.debug("fetchstreamState method start {}", id);
		}
		String url = baseUrl + "/live_streams/" + id + "/state";
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		LiveStreamState response;
		try {
			ResponseEntity<LiveStreamState> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request,
					LiveStreamState.class);
			response = responseEntity.getBody();
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.error(e.getMessage());
			}
			throw new AppException(e.getMessage());
		}
		FetchStreamUIResponse result = new FetchStreamUIResponse(response.getLiveStreamState().getState());
		if (log.isDebugEnabled()) {
			log.debug("fetchstreamState method end");
		}
		return result;
	}

	/**
	 * Save stream.
	 *
	 * @param response the response
	 */
	@Override
	public void saveStream(IVSLiveStreamResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("saveStream method start {}.", response);
		}
		Streams stream = new Streams();

		User newUser = userService.getUser();
		stream.setStreamId(response.getLiveStreamResponse().getId());
		stream.setApplicationName(
				response.getLiveStreamResponse().getDirect_playback_urls().getWebrtc().get(0).getApplication_name());
		stream.setStreamName(response.getLiveStreamResponse().getName());
		stream.setSourceStreamName(
				response.getLiveStreamResponse().getDirect_playback_urls().getWebrtc().get(0).getName());
		stream.setActive(true);
		stream.setStreamUrl(response.getLiveStreamResponse().getDirect_playback_urls().getWebrtc().get(0).getUrl());
		stream.setUser(newUser);
		streamRepo.save(stream);
		if (log.isDebugEnabled()) {
			log.debug("saveStream method end");
		}
	}

	/**
	 * Creates the stream target.
	 *
	 * @param streamTarget the stream target
	 * @return the string
	 */
	@Override
	public String createStreamTarget(StreamTargetModel streamTargetModel, String streamId) {
		if (log.isDebugEnabled()) {
			log.debug("createStreaTarget method start {}.", streamTargetModel);
		}
		String url = baseUrl + "/stream_targets/custom";
		MultiValueMap<String, String> headers = getHeader();
		StreamTargetDTO streamTargetDTO = new StreamTargetDTO();
		streamTargetDTO.setStreamTarget(streamTargetModel);

		HttpEntity<StreamTargetDTO> request = new HttpEntity<>(streamTargetDTO, headers);
		StreamTargetDTO streamTargetResponse;
		try {
			ResponseEntity<StreamTargetDTO> reponseEntity = restTemplate.exchange(url, HttpMethod.POST, request,
					StreamTargetDTO.class);
			streamTargetResponse = reponseEntity.getBody();
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.error(e.getMessage());
			}
			throw new AppException(e.getMessage());
		}
		// saving the response to mongodb
		streamTargetResponse.setStreamId(streamId);
		mongoStreamTargetRepo.save(streamTargetResponse);

		// saving the streamTargetId in the database
		Streams stream = streamRepo.findByStreamId(streamId);
		StreamTargetModel streamTarResponse = streamTargetResponse.getStreamTarget();
		StreamTarget streamTarget = new StreamTarget(streamTarResponse.getId(), streamTarResponse.getPrimary_url(),
				streamTarResponse.getStream_name(), streamTarResponse.getBackup_url(), stream);
		streamTargetRepo.save(streamTarget);
		if (log.isDebugEnabled()) {
			log.debug("createStreaTarget method end");
		}
		return streamTarResponse.getId();
	}

	/**
	 * Adds the stream target.
	 *
	 * @param transcoderId   the transcoder id
	 * @param outputId       the output id
	 * @param streamTargetId the stream target id
	 * @return true, if successful
	 */
	@Override
	public boolean addStreamTarget(String streamId, String outputId, String streamTargetId) {
		if (log.isDebugEnabled()) {
			log.debug("addStreamTarget method start");
		}
		String url = baseUrl + "/transcoders/" + streamId + "/outputs/" + outputId + "/output_stream_targets";
		MultiValueMap<String, String> headers = getHeader();

		OutputStreamTarget outputStreamTarget = new OutputStreamTarget(streamTargetId, true);
		OutputStreamTargetDTO outputStreamTargetDTO = new OutputStreamTargetDTO(outputStreamTarget);
		HttpEntity<OutputStreamTargetDTO> request = new HttpEntity<>(outputStreamTargetDTO, headers);
		OutputStreamTargetDTO response;
		try {
			ResponseEntity<OutputStreamTargetDTO> responseBody = restTemplate.exchange(url, HttpMethod.POST, request,
					OutputStreamTargetDTO.class);
			response = responseBody.getBody();
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.error(e.getMessage());
			}
			throw new AppException(e.getMessage());
		}
		// saving response to mongodb
		response.setStreamId(streamId);
		outputStreamTargetRepo.save(response);
		if (log.isDebugEnabled()) {
			log.debug("addStreamTarget method end");
		}
		return true;
	}

	@Override
	public boolean deleteStreamTarget(String streamTargetId) {
		if (log.isDebugEnabled()) {
			log.debug("deleteStreamTarget method start {}", streamTargetId);
		}
		String url = baseUrl + "/stream_targets/custom/" + streamTargetId;
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		try {
			restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.error(e.getMessage());
			}
			throw new AppException(e.getMessage());
		}
		if (log.isDebugEnabled()) {
			log.debug("deleteStreamTarget method end");
		}
		return true;
	}

	public Boolean deleteOutputTargetAll(List<Webrtc> webrtcList, String streamId) {
		if (log.isDebugEnabled()) {
			log.debug("deleteOutputTargetAll method start");
		}
		webrtcList.stream().skip(4).forEach(output -> {
			deleteOutputTarget(streamId, output.getOutput_id());
		});
		if (log.isDebugEnabled()) {
			log.debug("deleteOutputTargetAll method end");
		}
		return true;
	}

	public Boolean deleteOutputTarget(String streamId, String outputId) {
		if (log.isDebugEnabled()) {
			log.debug("deleteOutputTarget method start");
		}
		String url = baseUrl + "/transcoders/" + streamId + "/outputs/" + outputId;
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		try {
			restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.error(e.getMessage());
			}
			throw new AppException(e.getMessage());
		}
		if (log.isDebugEnabled()) {
			log.debug("deleteOutputTarget method end");
		}
		return true;
	}
}
