package com.arraigntech.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.arraigntech.entity.Streams;
import com.arraigntech.entity.User;
import com.arraigntech.model.IVSLiveStream;
import com.arraigntech.model.IVSLiveStreamResponse;
import com.arraigntech.model.LiveStreamState;
import com.arraigntech.mongorepos.StreamResponseRepository;
import com.arraigntech.repository.StreamRepository;
import com.arraigntech.repository.UserRespository;
import com.arraigntech.service.IVSStreamService;

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

	@Value("${wowza.url}")
	private String baseUrl;

	@Value("${wsc-api-key}")
	private String wscApiKey;

	@Value("${wsc-access-key}")
	private String wscAccessKey;

	@Autowired
	private StreamResponseRepository streamResponseRepo;

	public IVSLiveStreamResponse createStream(IVSLiveStream liveStream) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Content-Type", "application/json");
		headers.set("wsc-api-key", wscApiKey);
		headers.set("wsc-access-key", wscAccessKey);

		HttpEntity<IVSLiveStream> request = new HttpEntity<>(liveStream, headers);
		ResponseEntity<IVSLiveStreamResponse> reponseEntity = restTemplate.exchange(baseUrl, HttpMethod.POST, request,
				IVSLiveStreamResponse.class);
		IVSLiveStreamResponse liveStreamResponse = reponseEntity.getBody();

		// saving the response data to mongodb
		streamResponseRepo.save(liveStreamResponse);

		//saving necessary details to postgresql
		saveStream(liveStreamResponse);
		
		//checking whether stream has been started completly, if started return the response
		String streamId = liveStreamResponse.getLiveStreamResponse().getId();
		startStream(streamId);

		boolean flag = true;
		while (flag) {
			LiveStreamState response = fetchStreamState(streamId);
			if (response.getLiveStreamState().getState().equals(STARTING)) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				flag = false;
			}
		}

		return liveStreamResponse;
	}

	public String startStream(String id) {
		String url = baseUrl + "/" + id + "/start";
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Content-Type", "application/json");
		headers.set("wsc-api-key", wscApiKey);
		headers.set("wsc-access-key", wscAccessKey);

		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
		String response = responseEntity.getBody();

		Streams stream = streamRepo.findByStreamId(id);
		stream.setStatus(STARTED);
		streamRepo.save(stream);

		return response;
	}

	public String stopStream(String id) {
		String url = baseUrl + "/" + id + "/stop";
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Content-Type", "application/json");
		headers.set("wsc-api-key", wscApiKey);
		headers.set("wsc-access-key", wscAccessKey);

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
		String url = "https://api.cloud.wowza.com/api/v1.6/live_streams/" + id + "/state";
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Content-Type", "application/json");
		headers.set("wsc-api-key", wscApiKey);
		headers.set("wsc-access-key", wscAccessKey);

		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<LiveStreamState> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request,
				LiveStreamState.class);
		LiveStreamState response = responseEntity.getBody();
		return response;
	}

	// saving the necessary stream details to stream database
	public void saveStream(IVSLiveStreamResponse response) {
		Streams stream = new Streams();

//		 getting the authenticated user from security context
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		String str=authentication.getName();
		User user=userRepo.findByEmail("bhaskaras1999@gmail.com");
		stream.setStreamId(response.getLiveStreamResponse().getId());
		stream.setApplicationName(
				response.getLiveStreamResponse().getDirect_playback_urls().getWebrtc().get(0).getApplication_name());
		stream.setStreamName(response.getLiveStreamResponse().getName());
		stream.setSourceStreamName(
				response.getLiveStreamResponse().getDirect_playback_urls().getWebrtc().get(0).getName());
		stream.setStatus(STARTED);
		stream.setStreamUrl(response.getLiveStreamResponse().getDirect_playback_urls().getWebrtc().get(0).getUrl());
		stream.setUser(user);
		streamRepo.save(stream);

	}

}
