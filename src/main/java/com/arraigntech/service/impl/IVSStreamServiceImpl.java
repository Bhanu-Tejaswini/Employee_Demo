/*
 * 
 */
package com.arraigntech.service.impl;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.arraigntech.Exception.AppException;
import com.arraigntech.entity.Channels;
import com.arraigntech.entity.Streams;
import com.arraigntech.entity.User;
import com.arraigntech.mongorepos.StreamResponseRepository;
import com.arraigntech.repository.ChannelsRepository;
import com.arraigntech.repository.StreamRepository;
import com.arraigntech.repository.UserRespository;
import com.arraigntech.service.IVSStreamService;
import com.arraigntech.streamsModel.IVSLiveStream;
import com.arraigntech.streamsModel.IVSLiveStreamResponse;
import com.arraigntech.streamsModel.LiveStream;
import com.arraigntech.streamsModel.LiveStreamState;
import com.arraigntech.streamsModel.OutputStreamTarget;
import com.arraigntech.streamsModel.OutputStreamTargetDTO;
import com.arraigntech.streamsModel.StreamSourceConnectionInformation;
import com.arraigntech.streamsModel.StreamTarget;
import com.arraigntech.streamsModel.StreamTargetDTO;
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
	public static final String BILLINT_MODE="pay_as_you_go";
	public static final String BROADCAST_LOCATION="asia_pacific_india";
	public static final String ENCODER="other_webrtc";
	public static final String TRANSCODER_TYPE="transcoded";
	public static final String DELIVERY_METHOD="push";
	public static final String DELIVERY_TYPE="single-bitrate";
	public static final boolean PLAYER_RESPONSIVE=true;
	public static final boolean RECORDING=false; 
	public static final String STARTING = "starting";
	public static final String STARTED = "started";
	public static final String STOPPED = "stopped";
	@Autowired
	private RestTemplate restTemplate;

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

	@Autowired
	private StreamResponseRepository streamResponseRepo;

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
	@Transactional
	public StreamUIResponse createStream(StreamUIRequest streamRequest) {
		log.debug("create stream start");
		IVSLiveStream liveStream=populateStreamData(streamRequest);
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
		youtubeStream(streamId, outputId);
		startStream(streamId);

		boolean flag = true;
		while (flag) {
			LiveStreamState response = fetchStreamState(streamId);
			if (response.getLiveStreamState().getState().equals(STARTING)) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					throw new AppException(e.getMessage());
				}
			} else {
				flag = false;
			}
		}
		log.debug("create stream end");
		StreamSourceConnectionInformation response = liveStreamResponse.getLiveStreamResponse()
				.getSource_connection_information();
		return new StreamUIResponse(response.getSdp_url(), response.getApplication_name(), response.getStream_name(),
				streamId);
	}


	/**
	 * @param streamRequest
	 * @return
	 */
	
	private IVSLiveStream populateStreamData(StreamUIRequest streamRequest) {
		IVSLiveStream ivsLiveStream=new IVSLiveStream();
		LiveStream liveStream=new LiveStream();
		liveStream.setAspect_ratio_height(streamRequest.getAspectRatioHeight());
		liveStream.setAspect_ratio_width(streamRequest.getAspectRatioWidth());
		liveStream.setBilling_mode(BILLINT_MODE);
		liveStream.setBroadcast_location(BROADCAST_LOCATION);
		liveStream.setDelivery_method(DELIVERY_METHOD);
		liveStream.setDelivery_type(DELIVERY_TYPE);
		liveStream.setEncoder(ENCODER);
		liveStream.setName("STREAM_"+RandomIdGenerator.generate());
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
	public boolean deleteStream(String streamId) {
		log.debug("deleteStream start");
		String url = baseUrl + "/live_streams/" + streamId;
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		try {
			restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
		} catch (Exception e) {
			log.error("error occurred while deleting the stream");
			throw new AppException(e.getMessage());
		}
		log.debug("deleteStream end");
		return true;
	}

	/**
	 * Youtube stream.
	 *
	 * @param streamId the stream id
	 * @param outputId the output id
	 * @return true, if successful
	 */
	/**
	 *
	 */
	public boolean youtubeStream(String streamId, String outputId) {
		log.debug("youtubestream start");
		User newUser = getUser();
		List<Channels> userChannels = channelRepo.findByUserAndType(newUser, ChannelTypeProvider.YOUTUBE);
		if(userChannels.isEmpty()) {
			deleteStream(streamId);
			throw new AppException("Please add the channels to start the live stream.");
		}
		userChannels.forEach(channel -> {
			String streamTargetId = createStreamTarget(new StreamTarget("STREAMTARGET_" + RandomIdGenerator.generate(),
					channel.getPrimaryUrl(), channel.getStreamName(), channel.getBackupUrl()));
			addStreamTarget(streamId, outputId, streamTargetId);
		});
		log.debug("youtubestream end");
		return true;
	}

	/**
	 * Start stream.
	 *
	 * @param id the id
	 * @return the string
	 */
	public String startStream(String id) {
		log.debug("startStream start");
		String url = baseUrl + "/live_streams/" + id + "/start";
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		String response = null;
		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
			response = responseEntity.getBody();
		} catch (Exception e) {
			log.error("error occurred while starting the live stream");
			throw new AppException(e.getMessage());
		}
		Streams stream = streamRepo.findByStreamId(id);
		stream.setStatus(STARTED);
		streamRepo.save(stream);
		log.debug("startStream end");
		return response;
	}

	/**
	 * Stop stream.
	 *
	 * @param id the id
	 * @return the string
	 */
	public String stopStream(String id) {
		log.debug("stopStream start");
		String url = baseUrl + "/live_streams/" + id + "/stop";
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		String response = null;
		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
			response = responseEntity.getBody();
		} catch (Exception e) {
			log.error("error occurred while stoping the live stream");
			throw new AppException(e.getMessage());
		}
		// updating the status of the stream
		Streams stream = streamRepo.findByStreamId(id);
		stream.setStatus(STOPPED);
		streamRepo.save(stream);
		log.debug("stopStream end");
		return response;
	}

	/**
	 * Fetch stream state.
	 *
	 * @param id the id
	 * @return the live stream state
	 */
	public LiveStreamState fetchStreamState(String id) {
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
		return response;
	}

	/**
	 * Save stream.
	 *
	 * @param response the response
	 */
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
		stream.setStatus(STARTED);
		stream.setStreamUrl(response.getLiveStreamResponse().getDirect_playback_urls().getWebrtc().get(0).getUrl());
		stream.setUser(newUser);
		streamRepo.save(stream);
		log.debug("saveStream end");
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

	/**
	 * Creates the stream target.
	 *
	 * @param streamTarget the stream target
	 * @return the string
	 */
	public String createStreamTarget(StreamTarget streamTarget) {
		log.debug("createStreamTarget start");
		String url = baseUrl + "/stream_targets/custom";
		MultiValueMap<String, String> headers = getHeader();
		System.out.println(url);
		streamTarget.setProvider("rtmp");
		StreamTargetDTO streamTargetDTO = new StreamTargetDTO();
		streamTargetDTO.setStreamTarget(streamTarget);
		// setting the provider to rtmp

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
		log.debug("createStreaTarget end");
		return streamTargetResponse.getStreamTarget().getId();
	}

	/**
	 * Adds the stream target.
	 *
	 * @param transcoderId the transcoder id
	 * @param outputId the output id
	 * @param streamTargetId the stream target id
	 * @return true, if successful
	 */
	public boolean addStreamTarget(String transcoderId, String outputId, String streamTargetId) {
		log.debug("addStreamTarget start");
		String url = baseUrl + "/transcoders/" + transcoderId + "/outputs/" + outputId + "/output_stream_targets";
		MultiValueMap<String, String> headers = getHeader();

		OutputStreamTarget outputStreamTarget = new OutputStreamTarget(streamTargetId, true);
		OutputStreamTargetDTO outputStreamTargetDTO = new OutputStreamTargetDTO(outputStreamTarget);
		HttpEntity<OutputStreamTargetDTO> request = new HttpEntity<>(outputStreamTargetDTO, headers);
		try {
			restTemplate.exchange(url, HttpMethod.POST, request, OutputStreamTargetDTO.class);
		} catch (Exception e) {
			log.error("error occurred while adding stream target");
			throw new AppException(e.getMessage());
		}
		log.debug("addStreamTarget end");
		return true;
	}
}
