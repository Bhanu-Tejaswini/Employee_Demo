package com.arraigntech.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

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

import com.arraigntech.Exception.AppException;
import com.arraigntech.entity.Channels;
import com.arraigntech.entity.StreamTarget;
import com.arraigntech.entity.Streams;
import com.arraigntech.entity.User;
import com.arraigntech.mongorepos.MongoStreamResponseRepository;
import com.arraigntech.mongorepos.MongoStreamTargetRepository;
import com.arraigntech.mongorepos.OutputStreamTargetRepository;
import com.arraigntech.repository.ChannelsRepository;
import com.arraigntech.repository.StreamRepository;
import com.arraigntech.repository.StreamTargetRepository;
import com.arraigntech.repository.UserRespository;
import com.arraigntech.service.IVSStreamService;
import com.arraigntech.streamsModel.FacebookStreamRequest;
import com.arraigntech.streamsModel.FacebookStreamResponse;
import com.arraigntech.streamsModel.FetchStreamUIResponse;
import com.arraigntech.streamsModel.IVSLiveStream;
import com.arraigntech.streamsModel.IVSLiveStreamResponse;
import com.arraigntech.streamsModel.LiveStream;
import com.arraigntech.streamsModel.LiveStreamState;
import com.arraigntech.streamsModel.OutputStreamTarget;
import com.arraigntech.streamsModel.OutputStreamTargetDTO;
import com.arraigntech.streamsModel.StreamSourceConnectionInformation;
import com.arraigntech.streamsModel.StreamTargetDTO;
import com.arraigntech.streamsModel.StreamTargetModel;
import com.arraigntech.streamsModel.StreamUIRequest;
import com.arraigntech.streamsModel.StreamUIResponse;
import com.arraigntech.utility.ChannelTypeProvider;
import com.arraigntech.utility.CommonUtils;
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
	private static final String STARTING = "starting";
	private static final String STATUS = "status";
	private static final String LIVE_NOW = "LIVE_NOW";
	private static final String ACCESS_TOKEN = "access_token";
	public static final String RTMPS="rtmps";
	public static final String RTMP="rtmp";
	private static final String STOPPED="stopped";

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Executor executorService;
	@Autowired
	private StreamRepository streamRepo;

	@Autowired
	private UserRespository userRepo;

	@Autowired
	private ChannelsRepository channelRepo;

	@Value("${wowza.url}")
	private String baseUrl;

	@Value("${wsc-api-key}")
	private String wscApiKey;

	@Value("${wsc-access-key}")
	private String wscAccessKey;

	@Value("${facebook.streamurl}")
	private String graphUrl;

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
		log.debug("wowza header start");
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Content-Type", "application/json");
		headers.set("wsc-api-key", wscApiKey);
		headers.set("wsc-access-key", wscAccessKey);
		log.debug("wowza header end");
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
		log.debug("create stream start");

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
			log.error("error occurred while creating the live stream");
			throw new AppException(e.getMessage());
		}

		// saving the response data to mongodb
		streamResponseRepo.save(liveStreamResponse);
		// saving necessary details to postgresql
		saveStream(liveStreamResponse);

		// checking whether stream has been started completly, if started return the
		// response
		String streamId = liveStreamResponse.getLiveStreamResponse().getId();
		String outputId = liveStreamResponse.getLiveStreamResponse().getDirect_playback_urls().getWebrtc().get(3)
				.getOutput_id();
		// adds youtube channels in the target
		channelsStream(streamId, outputId);
		CompletableFuture.runAsync(() -> startStream(streamId));
		
		StreamSourceConnectionInformation response = liveStreamResponse.getLiveStreamResponse()
				.getSource_connection_information();
		log.debug("create stream end");
		return new StreamUIResponse(response.getSdp_url(), response.getApplication_name(), response.getStream_name(),
				streamId,"starting");
	}

	/**
	 * @param streamRequest
	 * @return
	 */
	private IVSLiveStream populateStreamData(StreamUIRequest streamRequest) {
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
		log.debug("deleteStream start");
		Streams stream = streamRepo.findByStreamId(streamId);
		if (stream.isActive()) {
			if(!fetchStreamState(streamId).equals(STOPPED))
			stopStream(streamId);
		}
		String url = baseUrl + "/live_streams/" + streamId;
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		try {
			restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
		} catch (Exception e) {
			log.error("error occurred while deleting the stream");
			throw new AppException(e.getMessage());
		}
		// Also deleting the stream target
		stream.getStreamTarget().stream().forEach(streamTarget -> deleteStreamTarget(streamTarget.getStreamTargetId()));
		log.debug("deleteStream end");
		return true;
	}

	public Boolean channelsStream(String streamId, String outputId) {
		log.debug("channelStream method start");
		User newUser = getUser();
		List<Channels> youtubeChannels = channelRepo.findByUserAndType(newUser, ChannelTypeProvider.YOUTUBE);
		List<Channels> facebookChannels = channelRepo.findByUserAndType(newUser, ChannelTypeProvider.FACEBOOK);

		// if there are no channels added means
		if (youtubeChannels.isEmpty() && facebookChannels.isEmpty()) {
			deleteStream(streamId);
			throw new AppException(MessageConstants.NO_CHANNELS_TO_STREAM);
		}
		CompletableFuture.runAsync(() -> youtubeStream(youtubeChannels, streamId, outputId),executorService);
		CompletableFuture.runAsync(() -> facebookStream(facebookChannels, streamId, outputId),executorService);
		
		log.debug("channelStream method end");
		return true;
	}

	/**
	 * @param facebookChannels
	 * @param streamId
	 * @param outputId
	 */
	private void facebookStream(List<Channels> facebookChannels, String streamId, String outputId) {
		log.debug("facebookStream start");
		facebookChannels.stream().forEach(channel -> {
			FacebookStreamRequest facebookStreamRequest = getFacebookStreamData(channel);
			String streamTargetId = createStreamTarget(
					new StreamTargetModel("FACEBOOK_" + RandomIdGenerator.generate(5),RTMPS,
							facebookStreamRequest.getPrimaryUrl(), facebookStreamRequest.getStreamName(), facebookStreamRequest.getPrimaryUrl()),
					streamId);
			CompletableFuture.runAsync(() -> addStreamTarget(streamId, outputId, streamTargetId));
		});
		log.debug("facebookStream end");
	}

	/**
	 * @return
	 */
	private FacebookStreamRequest getFacebookStreamData(Channels channel) {
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
			throw new AppException(e.getMessage());
		}

		String[] responseData = facebookStreamResponse.getStream_url().split("/");
		FacebookStreamRequest facebookStreamRequest = new FacebookStreamRequest();
		facebookStreamRequest.setPrimaryUrl(responseData[0] + "//" + responseData[2] + "/" + responseData[3] + "/");
		facebookStreamRequest.setStreamName(responseData[4]);
		log.debug("getFacebookStreamData end");
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
		log.debug("youtubestream start");
		youtubeChannels.stream().forEach(channel -> {
			String streamTargetId = createStreamTarget(
					new StreamTargetModel("YOUTUBE_" + RandomIdGenerator.generate(5),RTMP, channel.getPrimaryUrl(),
							channel.getStreamName(), channel.getBackupUrl()),
					streamId);
			CompletableFuture.runAsync(() -> addStreamTarget(streamId, outputId, streamTargetId));
		});
		log.debug("youtubestream end");
	}

	/**
	 * Start stream.
	 *
	 * @param id the id
	 * @return the string
	 */
	@Override
	public void startStream(String id) {
		log.debug("startStream start");
		String url = baseUrl + "/live_streams/" + id + "/start";
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		LiveStreamState response;
		try {
			ResponseEntity<LiveStreamState> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, LiveStreamState.class);
			response = responseEntity.getBody();
		} catch (Exception e) {
			log.error("error occurred while starting the live stream");
			throw new AppException(e.getMessage());
		}
		Streams stream = streamRepo.findByStreamId(id);
		stream.setActive(true);
		;
		streamRepo.save(stream);
		log.debug("startStream end");
//		return response.getLiveStreamState().getState();
	}

	/**
	 * Stop stream.
	 *
	 * @param id the id
	 * @return the string
	 */
	@Override
	public String stopStream(String id) {
		log.debug("stopStream start");
		String url = baseUrl + "/live_streams/" + id + "/stop";
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		LiveStreamState response;
		try {
			ResponseEntity<LiveStreamState> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, LiveStreamState.class);
			response = responseEntity.getBody();
		} catch (Exception e) {
			log.error("error occurred while stoping the live stream");
			throw new AppException(e.getMessage());
		}
		// updating the status of the stream
		Streams stream = streamRepo.findByStreamId(id);
		stream.setActive(false);
		streamRepo.save(stream);
		log.debug("stopStream end");
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
		log.debug("fetchStreamState start");
		String url = baseUrl + "/live_streams/" + id + "/state";
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		LiveStreamState response;
		try {
			ResponseEntity<LiveStreamState> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request,
					LiveStreamState.class);
			response = responseEntity.getBody();
		} catch (Exception e) {
			log.error("error occurred while fetching stream state");
			throw new AppException(e.getMessage());
		}
		log.debug("fetchstreamState end");
		FetchStreamUIResponse result= new FetchStreamUIResponse(response.getLiveStreamState().getState());
		return result;
	}

	/**
	 * Save stream.
	 *
	 * @param response the response
	 */
	@Override
	public void saveStream(IVSLiveStreamResponse response) {
		log.debug("saveStream start");
		Streams stream = new Streams();

		User newUser = getUser();
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
		log.debug("saveStream end");
	}

	/**
	 * Creates the stream target.
	 *
	 * @param streamTarget the stream target
	 * @return the string
	 */
	@Override
	public String createStreamTarget(StreamTargetModel streamTargetModel, String streamId) {
		log.debug("createStreamTarget start");
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
			log.error("error occurred while creating stream target");
			throw new AppException(e.getMessage());
		}
		// saving the response to mongodb
		mongoStreamTargetRepo.save(streamTargetResponse);

		// saving the streamTargetId in the database
		Streams stream = streamRepo.findByStreamId(streamId);
		StreamTargetModel streamTarResponse = streamTargetResponse.getStreamTarget();
		StreamTarget streamTarget = new StreamTarget(streamTarResponse.getId(), streamTarResponse.getPrimary_url(),
				streamTarResponse.getStream_name(), streamTarResponse.getBackup_url(), stream);
		try {
		streamTargetRepo.save(streamTarget);}
		catch(Exception e) {
			System.out.println(e);
		}
		log.debug("createStreaTarget end");
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
	public boolean addStreamTarget(String transcoderId, String outputId, String streamTargetId) {
		log.debug("addStreamTarget start");
		String url = baseUrl + "/transcoders/" + transcoderId + "/outputs/" + outputId + "/output_stream_targets";
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
			log.error("error occurred while adding stream target");
			throw new AppException(e.getMessage());
		}
		// saving response to mongodb
		outputStreamTargetRepo.save(response);
		log.debug("addStreamTarget end");
		return true;
	}

	@Override
	public boolean deleteStreamTarget(String streamTargetId) {
		log.debug("deleteStreamTarget start");

		String url = baseUrl + "/stream_targets/custom/" + streamTargetId;
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		try {
			restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
		} catch (Exception e) {
			log.error("error occurred while deleting the streamTarget");
			throw new AppException(e.getMessage());
		}
		log.debug("deleteStreamTarget end");
		return true;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	private User getUser() {
		User user = userRepo.findByEmail(CommonUtils.getUser());
		if (Objects.isNull(user)) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		return user;
	}
}
