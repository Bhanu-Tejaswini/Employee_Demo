package com.arraigntech.service;

import java.util.List;

import com.arraigntech.entity.Channels;
import com.arraigntech.streamsModel.FetchStreamUIResponse;
import com.arraigntech.streamsModel.IVSLiveStreamResponse;
import com.arraigntech.streamsModel.StreamTargetModel;
import com.arraigntech.streamsModel.StreamUIRequest;
import com.arraigntech.streamsModel.StreamUIResponse;

public interface IVSStreamService {

	public StreamUIResponse createStream(StreamUIRequest streamRequest);
	
	public String startStream(String id);
	
	public String stopStream(String id);
	
	public boolean deleteStream(String streamId);
	
	public FetchStreamUIResponse fetchStreamState(String id);
	
	public void saveStream(IVSLiveStreamResponse response);
	
	public void youtubeStream(List<Channels> youtubeChannels, String streamId, String outputId);
	
	public String createStreamTarget(StreamTargetModel streamTarget, String streamId);
	
	public boolean deleteStreamTarget(String streamId);
	
	public boolean addStreamTarget(String transcoderId, String outputId, String streamTargetId);

}
