package com.arraigntech.service;

import java.util.List;

import com.arraigntech.entity.Channels;
import com.arraigntech.entity.StreamTarget;
import com.arraigntech.streams.model.FetchStreamUIResponse;
import com.arraigntech.streams.model.IVSLiveStreamResponse;
import com.arraigntech.streams.model.StreamTargetModel;
import com.arraigntech.streams.model.StreamUIRequest;
import com.arraigntech.streams.model.StreamUIResponse;

public interface IVSStreamService {

	public StreamUIResponse createStream(StreamUIRequest streamRequest);
	
	public void startStream(String id);
	
	public String stopStream(String id);
	
	public boolean deleteStream(String streamId);
	
	public FetchStreamUIResponse fetchStreamState(String id);
	
	public void saveStream(IVSLiveStreamResponse response);
	
	public void youtubeStream(List<Channels> youtubeChannels, String streamId, String outputId);
	
	public String createStreamTarget(StreamTargetModel streamTarget, String streamId);
	
	public boolean deleteStreamTarget(String streamId);
	
	public boolean addStreamTarget(String transcoderId, String outputId, String streamTargetId);

}
