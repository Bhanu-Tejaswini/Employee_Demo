package com.arraigntech.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
import com.arraigntech.streamsModel.LiveStreamState;
import com.arraigntech.streamsModel.OutputStreamTarget;
import com.arraigntech.streamsModel.OutputStreamTargetDTO;
import com.arraigntech.streamsModel.StreamSourceConnectionInformation;
import com.arraigntech.streamsModel.StreamTarget;
import com.arraigntech.streamsModel.StreamTargetDTO;
import com.arraigntech.utility.ChannelTypeProvider;
import com.arraigntech.utility.CommonUtils;
import com.arraigntech.utility.MessageConstants;
import com.arraigntech.utility.RandomIdGenerator;

@Service
public class IVSStreamServiceImpl implements IVSStreamService {

	final static String STARTING = "starting";
	final static String STARTED = "started";
	final static String STOPPED = "stopped";

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

	public MultiValueMap<String, String> getHeader() {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Content-Type", "application/json");
		headers.set("wsc-api-key", wscApiKey);
		headers.set("wsc-access-key", wscAccessKey);
		return headers;
	}

	public StreamSourceConnectionInformation createStream(IVSLiveStream liveStream) {
		String url = baseUrl + "/live_streams";
		MultiValueMap<String, String> headers = getHeader();
		HttpEntity<IVSLiveStream> request = new HttpEntity<>(liveStream, headers);
		ResponseEntity<IVSLiveStreamResponse> reponseEntity = restTemplate.exchange(url, HttpMethod.POST, request,
				IVSLiveStreamResponse.class);
		IVSLiveStreamResponse liveStreamResponse = reponseEntity.getBody();

		// saving the response data to mongodb
		streamResponseRepo.save(liveStreamResponse);

		// saving necessary details to postgresql
		saveStream(liveStreamResponse);

		// checking whether stream has been started completly, if started return the
		// response
		String streamId = liveStreamResponse.getLiveStreamResponse().getId();
		String outputId=liveStreamResponse.getLiveStreamResponse().getDirect_playback_urls().getWebrtc().get(3).getOutput_id();
		//adds youtube channels in the target
		youtubeStream(streamId,outputId);
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

		return liveStreamResponse.getLiveStreamResponse().getSource_connection_information();
	}
	
	public boolean youtubeStream(String streamId, String outputId) {
		User newUser=getUser();
		List<Channels> userChannels = channelRepo.findByUserAndType(newUser, ChannelTypeProvider.YOUTUBE);
		userChannels.forEach(channel->{
			String streamTargetId = createStreamTarget(new StreamTarget("streamtarget"+RandomIdGenerator.generate(),channel.getPrimaryUrl(),channel.getStreamName(),channel.getBackupUrl()));
			addStreamTarget(streamId,outputId,streamTargetId);
		});
		return true;
	}

	public String startStream(String id) {
		String url = baseUrl + "/live_streams/" + id + "/start";
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
		String response = responseEntity.getBody();

		Streams stream = streamRepo.findByStreamId(id);
		stream.setStatus(STARTED);
		streamRepo.save(stream);

		return response;
	}

	public String stopStream(String id) {
		String url = baseUrl + "/live_streams/" + id + "/stop";
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
		String response = responseEntity.getBody();

		// updating the status of the stream
		Streams stream = streamRepo.findByStreamId(id);
		stream.setStatus(STOPPED);
		streamRepo.save(stream);

		return response;
	}

	// To fetch the status of the stream whether stream started, starting or stopped
	public LiveStreamState fetchStreamState(String id) {
		String url = baseUrl + "/live_streams/" + id + "/state";
		MultiValueMap<String, String> headers = getHeader();

		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<LiveStreamState> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request,
				LiveStreamState.class);
		LiveStreamState response = responseEntity.getBody();
		return response;
	}

	// saving the necessary stream details to stream database
	public void saveStream(IVSLiveStreamResponse response) {
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

	}

	// returns currently logged in user details
	private User getUser() {
		User user = userRepo.findByEmail(CommonUtils.getUser());
		if (Objects.isNull(user)) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		return user;
	}

	// create stream target	
	public String createStreamTarget(StreamTarget streamTarget) {
		String url = baseUrl + "/stream_targets/custom";
		MultiValueMap<String, String> headers = getHeader();
		System.out.println(url);
		streamTarget.setProvider("rtmp");
		StreamTargetDTO streamTargetDTO=new StreamTargetDTO();
		streamTargetDTO.setStreamTarget(streamTarget);
		// setting the provider to rtmp
		
		HttpEntity<StreamTargetDTO> request = new HttpEntity<>(streamTargetDTO, headers);
		ResponseEntity<StreamTargetDTO> reponseEntity = restTemplate.exchange(url, HttpMethod.POST, request,
				StreamTargetDTO.class);
		StreamTargetDTO streamTargetResponse = reponseEntity.getBody();

		return streamTargetResponse.getStreamTarget().getId();
	}
	
	//ADD created stream target to existing output stream
	public boolean addStreamTarget(String transcoderId, String outputId, String streamTargetId) {
		String url = baseUrl +"/transcoders/"+transcoderId+"/outputs/"+outputId+"/output_stream_targets";
		MultiValueMap<String, String> headers = getHeader();

		OutputStreamTarget outputStreamTarget=new OutputStreamTarget(streamTargetId,true);
		OutputStreamTargetDTO outputStreamTargetDTO=new OutputStreamTargetDTO(outputStreamTarget);
		HttpEntity<OutputStreamTargetDTO> request = new HttpEntity<>(outputStreamTargetDTO, headers);
		ResponseEntity<OutputStreamTargetDTO> reponseEntity = restTemplate.exchange(url, HttpMethod.POST, request,
				OutputStreamTargetDTO.class);
//		OutputStreamTargetDTO outputStreamTargetDTO = reponseEntity.getBody()	
		return true;
	}

}
