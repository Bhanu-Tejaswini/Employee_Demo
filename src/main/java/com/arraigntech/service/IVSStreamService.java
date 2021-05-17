package com.arraigntech.service;

import com.arraigntech.streamsModel.IVSLiveStream;
import com.arraigntech.streamsModel.IVSLiveStreamResponse;
import com.arraigntech.streamsModel.LiveStreamState;
import com.arraigntech.streamsModel.StreamSourceConnectionInformation;
import com.arraigntech.streamsModel.StreamTargetModel;
import com.arraigntech.streamsModel.StreamUIRequest;
import com.arraigntech.streamsModel.StreamUIResponse;

public interface IVSStreamService {

	public StreamUIResponse createStream(StreamUIRequest streamRequest);
	
	public String startStream(String id);
	
	public String stopStream(String id);
	
	public boolean deleteStream(String streamId);
	
	public LiveStreamState fetchStreamState(String id);
	
	public void saveStream(IVSLiveStreamResponse response);
	
	public boolean youtubeStream(String streamId, String outputId);
	
	public String createStreamTarget(StreamTargetModel streamTarget, String streamId);
	
	public boolean deleteStreamTarget(String streamId);
	
	public boolean addStreamTarget(String transcoderId, String outputId, String streamTargetId);

}
