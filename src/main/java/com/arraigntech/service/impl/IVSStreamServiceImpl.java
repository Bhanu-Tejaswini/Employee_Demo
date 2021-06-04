package com.arraigntech.service.impl;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.arraigntech.entity.ChannelEntity;
import com.arraigntech.entity.StreamEntity;
import com.arraigntech.entity.StreamTargetEntity;
import com.arraigntech.entity.UserEntity;
import com.arraigntech.exceptions.AppException;
import com.arraigntech.mongorepos.MongoStreamResponseRepository;
import com.arraigntech.mongorepos.MongoStreamTargetRepository;
import com.arraigntech.mongorepos.OutputStreamTargetRepository;
import com.arraigntech.repository.StreamRepository;
import com.arraigntech.repository.StreamTargetRepository;
import com.arraigntech.request.vo.TranscoderRootVO;
import com.arraigntech.request.vo.TranscoderVO;
import com.arraigntech.response.vo.MongoUserVO;
import com.arraigntech.response.vo.S3UIResponse;
import com.arraigntech.service.ChannelService;
import com.arraigntech.service.DocumentS3Service;
import com.arraigntech.service.IVSStreamService;
import com.arraigntech.service.UserService;
import com.arraigntech.utility.ChannelTypeProvider;
import com.arraigntech.utility.MessageConstants;
import com.arraigntech.utility.RandomIdGenerator;
import com.arraigntech.wowza.request.vo.FacebookStreamRequestVO;
import com.arraigntech.wowza.request.vo.IVSLiveStreamVO;
import com.arraigntech.wowza.request.vo.LiveStreamVO;
import com.arraigntech.wowza.request.vo.StreamSourceConnectionInformationVO;
import com.arraigntech.wowza.request.vo.StreamUIRequestVO;
import com.arraigntech.wowza.response.vo.FacebookStreamResponseVO;
import com.arraigntech.wowza.response.vo.FetchStreamUIResponseVO;
import com.arraigntech.wowza.response.vo.IVSLiveStreamResponseVO;
import com.arraigntech.wowza.response.vo.LiveStreamStateVO;
import com.arraigntech.wowza.response.vo.OutputStreamTargetRootVO;
import com.arraigntech.wowza.response.vo.OutputStreamTargetVO;
import com.arraigntech.wowza.response.vo.StreamTargetRootVO;
import com.arraigntech.wowza.response.vo.StreamTargetVO;
import com.arraigntech.wowza.response.vo.StreamUIResponseVO;
import com.arraigntech.wowza.response.vo.WebrtcVO;

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
	private static final String RTMPS = "rtmps";
	private static final String RTMP = "rtmp";
	private static final String STOPPED = "stopped";
	private static final String TOP_RIGHT = "top-right";

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

	@Autowired
	private DocumentS3Service s3Service;

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
	public StreamUIResponseVO createStream(StreamUIRequestVO streamRequest) {
		UserEntity newUser = userService.getUser();
		IVSLiveStreamVO liveStream = populateStreamData(streamRequest);
		String url = baseUrl + "/live_streams";
		MultiValueMap<String, String> headers = getHeader();
		HttpEntity<IVSLiveStreamVO> request = new HttpEntity<>(liveStream, headers);
		IVSLiveStreamResponseVO liveStreamResponse;
		try {
			ResponseEntity<IVSLiveStreamResponseVO> reponseEntity = restTemplate.exchange(url, HttpMethod.POST, request,
					IVSLiveStreamResponseVO.class);
			liveStreamResponse = reponseEntity.getBody();
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.error(e.getMessage());
			}
			throw new AppException(e.getMessage());
		}
		// saving the response data to mongodb
		liveStreamResponse.setUser(new MongoUserVO(newUser.getId(), newUser.getEmail(), newUser.getUsername()));
		streamResponseRepo.save(liveStreamResponse);
		// saving necessary details to postgresql
		saveStream(liveStreamResponse);

		// checking whether stream has been started completly, if started return the
		// response
		String streamId = liveStreamResponse.getLiveStreamResponse().getId();
		List<WebrtcVO> webrtcList = liveStreamResponse.getLiveStreamResponse().getDirect_playback_urls().getWebrtc();
		// adds youtube channels in the target
		channelsStream(streamId, webrtcList);
		CompletableFuture.runAsync(() -> addLogoToTranscoder(streamId));
		CompletableFuture.runAsync(() -> startStream(streamId));
		CompletableFuture.runAsync(() -> deleteOutputTargetAll(webrtcList, streamId));
		StreamSourceConnectionInformationVO response = liveStreamResponse.getLiveStreamResponse()
				.getSource_connection_information();
		return new StreamUIResponseVO(response.getSdp_url(), response.getApplication_name(), response.getStream_name(),
				streamId, "starting");
	}

	/**
	 * @param streamRequest
	 * @return
	 * @throws IOException
	 */
	private IVSLiveStreamVO populateStreamData(StreamUIRequestVO streamRequest) {
		IVSLiveStreamVO ivsLiveStream = new IVSLiveStreamVO();
		LiveStreamVO liveStream = new LiveStreamVO();
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

	public void addLogoToTranscoder(String streamId) {
		S3UIResponse s3uiResponse = s3Service.getDocumentImageURL();
		byte[] byteArray = null;
		try {
			URL url = new URL(s3uiResponse.getImageUrl());
			byteArray = IOUtils.toByteArray(url.openConnection());
		} catch (IOException e1) {
			throw new AppException("Error occured while adding the image");
		}
		String encodeToString = Base64.getEncoder().encodeToString(byteArray);
		String url = baseUrl + "/transcoders/" + streamId;
		MultiValueMap<String, String> headers = getHeader();

		TranscoderVO transcoder = new TranscoderVO(true, 26, encodeToString, 100, TOP_RIGHT, 88);
		TranscoderRootVO transcoderRoot = new TranscoderRootVO(transcoder);
		HttpEntity<TranscoderRootVO> request = new HttpEntity<>(transcoderRoot, headers);
		RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		try {
			template.exchange(url, HttpMethod.PATCH, request, TranscoderRootVO.class);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.error(e.getMessage());
			}
			throw new AppException(e.getMessage());
		}
	}

	/**
	 * Delete stream.
	 *
	 * @param streamId the stream id
	 * @return true, if successful
	 */
	@Override
	public boolean deleteStream(String streamId) {
		StreamEntity stream = streamRepo.findByStreamId(streamId);
		if (Objects.isNull(stream)) {
			throw new AppException(MessageConstants.STREAM_NOT_EXISTS);
		}
		List<StreamTargetEntity> streamTargets = streamTargetRepo.findByStream(stream);
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
		return true;
	}

	public Boolean channelsStream(String streamId, List<WebrtcVO> webrtcList) {
		UserEntity newUser = userService.getUser();
		List<ChannelEntity> youtubeChannels = channelService.findByUserAndTypeAndActive(newUser,
				ChannelTypeProvider.YOUTUBE, true);
		List<ChannelEntity> facebookChannels = channelService.findByUserAndTypeAndActive(newUser,
				ChannelTypeProvider.FACEBOOK, true);
		List<ChannelEntity> instagramChannels = channelService.findByUserAndTypeAndActive(newUser,
				ChannelTypeProvider.INSTAGRAM, true);
		// if there are no channels added means
		if (youtubeChannels.isEmpty() && facebookChannels.isEmpty()) {
			deleteStream(streamId);
			throw new AppException(MessageConstants.NO_CHANNELS_TO_STREAM);
		}

		CompletableFuture.runAsync(() -> youtubeStream(youtubeChannels, streamId, webrtcList));
		CompletableFuture.runAsync(() -> facebookStream(facebookChannels, streamId, webrtcList.get(2).getOutput_id()))
				.handle((res, e) -> {
					throw new AppException(e.getMessage());
				});
		CompletableFuture
				.runAsync(() -> instagramStream(instagramChannels, streamId, webrtcList.get(2).getOutput_id()));
		return true;
	}

	/**
	 * @param instagramChannels
	 * @param streamId
	 * @param outputId
	 * @return
	 */
	public void instagramStream(List<ChannelEntity> instagramChannels, String streamId, String outputId) {
		instagramChannels.stream().forEach(channel -> {
			String streamTargetId = createStreamTarget(new StreamTargetVO("INSTGRAM_" + RandomIdGenerator.generate(5),
					RTMPS, channel.getPrimaryUrl(), channel.getStreamName(), channel.getBackupUrl()), streamId);
			CompletableFuture.runAsync(() -> addStreamTarget(streamId, outputId, streamTargetId));
		});
	}

	/**
	 * @param facebookChannels
	 * @param streamId
	 * @param outputId
	 */
	private void facebookStream(List<ChannelEntity> facebookChannels, String streamId, String outputId) {
		facebookChannels.stream().forEach(channel -> {
			FacebookStreamRequestVO facebookStreamRequest = getFacebookStreamData(channel);
			String streamTargetId = createStreamTarget(new StreamTargetVO("FACEBOOK_" + RandomIdGenerator.generate(5),
					RTMPS, facebookStreamRequest.getPrimaryUrl(), facebookStreamRequest.getStreamName(),
					facebookStreamRequest.getPrimaryUrl()), streamId);
			CompletableFuture.runAsync(() -> addStreamTarget(streamId, outputId, streamTargetId));
		});
	}

	/**
	 * @return
	 */
	private FacebookStreamRequestVO getFacebookStreamData(ChannelEntity channel) {
		log.debug("getFacebookStreamData start");
		String uri = graphUrl + channel.getFacebookUserId() + "/live_videos";
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		String url = builder.scheme("https").host(uri).queryParam(STATUS, LIVE_NOW)
				.queryParam(ACCESS_TOKEN, channel.getAccessToken()).build().toString();

		FacebookStreamResponseVO facebookStreamResponse;
		try {
			ResponseEntity<FacebookStreamResponseVO> responseBody = restTemplate.exchange(url, HttpMethod.POST,
					HttpEntity.EMPTY, FacebookStreamResponseVO.class);
			facebookStreamResponse = responseBody.getBody();
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.error(e.getMessage());
			}
			throw new AppException(e.getMessage());

		}
		String[] responseData = facebookStreamResponse.getStream_url().split("/");
		FacebookStreamRequestVO facebookStreamRequest = new FacebookStreamRequestVO();
		facebookStreamRequest.setPrimaryUrl(responseData[0] + "//" + responseData[2] + "/" + responseData[3] + "/");
		facebookStreamRequest.setStreamName(responseData[4]);
		return facebookStreamRequest;
	}

	/**
	 * Youtube stream.
	 * 
	 * @param youtubeChannels
	 *
	 * @param streamId        the stream id
	 * @param webrtcList      the output id
	 * @return true, if successful
	 */
	@Override
	public void youtubeStream(List<ChannelEntity> youtubeChannels, String streamId, List<WebrtcVO> webrtcList) {
		youtubeChannels.stream().forEach(channel -> {
			String streamTargetId = createStreamTarget(new StreamTargetVO("YOUTUBE_" + RandomIdGenerator.generate(5),
					RTMP, channel.getPrimaryUrl(), channel.getStreamName(), channel.getBackupUrl()), streamId);
			CompletableFuture
					.runAsync(() -> addStreamTarget(streamId, webrtcList.get(3).getOutput_id(), streamTargetId));
		});
	}

	/**
	 * Start stream.
	 *
	 * @param id the id
	 * @return the string
	 */
	@Override
	public void startStream(String id) {
		String url = baseUrl + "/live_streams/" + id + "/start";
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		LiveStreamStateVO response;
		try {
			ResponseEntity<LiveStreamStateVO> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request,
					LiveStreamStateVO.class);
			response = responseEntity.getBody();
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.error(e.getMessage());
			}
			throw new AppException(e.getMessage());
		}
		StreamEntity stream = streamRepo.findByStreamId(id);
		stream.setActive(true);
		streamRepo.save(stream);
	}

	/**
	 * Stop stream.
	 *
	 * @param id the id
	 * @return the string
	 */
	@Override
	public String stopStream(String id) {
		String url = baseUrl + "/live_streams/" + id + "/stop";
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		LiveStreamStateVO response;
		try {
			ResponseEntity<LiveStreamStateVO> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request,
					LiveStreamStateVO.class);
			response = responseEntity.getBody();
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.error(e.getMessage());
			}
			throw new AppException(e.getMessage());
		}
		// updating the status of the stream
		StreamEntity stream = streamRepo.findByStreamId(id);
		stream.setActive(false);
		streamRepo.save(stream);
		return response.getLiveStreamState().getState();
	}

	/**
	 * Fetch stream state.
	 *
	 * @param id the id
	 * @return the live stream state
	 */
	@Override
	public FetchStreamUIResponseVO fetchStreamState(String id) {
		String url = baseUrl + "/live_streams/" + id + "/state";
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		LiveStreamStateVO response;
		try {
			ResponseEntity<LiveStreamStateVO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request,
					LiveStreamStateVO.class);
			response = responseEntity.getBody();
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.error(e.getMessage());
			}
			throw new AppException(e.getMessage());
		}
		FetchStreamUIResponseVO result = new FetchStreamUIResponseVO(response.getLiveStreamState().getState());
		return result;
	}

	/**
	 * Save stream.
	 *
	 * @param response the response
	 */
	@Override
	public void saveStream(IVSLiveStreamResponseVO response) {
		StreamEntity stream = new StreamEntity();

		UserEntity newUser = userService.getUser();
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
	}

	/**
	 * Creates the stream target.
	 *
	 * @param streamTarget the stream target
	 * @return the string
	 */
	@Override
	public String createStreamTarget(StreamTargetVO streamTargetModel, String streamId) {
		String url = baseUrl + "/stream_targets/custom";
		MultiValueMap<String, String> headers = getHeader();
		StreamTargetRootVO streamTargetDTO = new StreamTargetRootVO();
		streamTargetDTO.setStreamTarget(streamTargetModel);

		HttpEntity<StreamTargetRootVO> request = new HttpEntity<>(streamTargetDTO, headers);
		StreamTargetRootVO streamTargetResponse;
		try {
			ResponseEntity<StreamTargetRootVO> reponseEntity = restTemplate.exchange(url, HttpMethod.POST, request,
					StreamTargetRootVO.class);
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
		StreamEntity stream = streamRepo.findByStreamId(streamId);
		StreamTargetVO streamTarResponse = streamTargetResponse.getStreamTarget();
		StreamTargetEntity streamTarget = new StreamTargetEntity(streamTarResponse.getId(),
				streamTarResponse.getPrimary_url(), streamTarResponse.getStream_name(),
				streamTarResponse.getBackup_url(), stream);
		streamTargetRepo.save(streamTarget);
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
		String url = baseUrl + "/transcoders/" + streamId + "/outputs/" + outputId + "/output_stream_targets";
		MultiValueMap<String, String> headers = getHeader();

		OutputStreamTargetVO outputStreamTarget = new OutputStreamTargetVO(streamTargetId, true);
		OutputStreamTargetRootVO outputStreamTargetDTO = new OutputStreamTargetRootVO(outputStreamTarget);
		HttpEntity<OutputStreamTargetRootVO> request = new HttpEntity<>(outputStreamTargetDTO, headers);
		OutputStreamTargetRootVO response;
		try {
			ResponseEntity<OutputStreamTargetRootVO> responseBody = restTemplate.exchange(url, HttpMethod.POST, request,
					OutputStreamTargetRootVO.class);
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
		return true;
	}

	@Override
	public boolean deleteStreamTarget(String streamTargetId) {
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
		return true;
	}

	public Boolean deleteOutputTargetAll(List<WebrtcVO> webrtcList, String streamId) {
		webrtcList.stream().skip(4).forEach(output -> {
			deleteOutputTarget(streamId, output.getOutput_id());
		});
		return true;
	}

	public Boolean deleteOutputTarget(String streamId, String outputId) {
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
		return true;
	}
}
