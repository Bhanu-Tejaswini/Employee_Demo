package com.arraigntech.service;

import com.arraigntech.streamsModel.IVSLiveStream;
import com.arraigntech.streamsModel.IVSLiveStreamResponse;
import com.arraigntech.streamsModel.LiveStreamState;
import com.arraigntech.streamsModel.StreamSourceConnectionInformation;
import com.arraigntech.streamsModel.StreamTarget;

public interface IVSStreamService {

	public StreamSourceConnectionInformation createStream(IVSLiveStream liveStream);
	
	public String startStream(String id);
	
	public String stopStream(String id);
	
	public LiveStreamState fetchStreamState(String id);
	
	public void saveStream(IVSLiveStreamResponse response);
	
	public boolean youtubeStream(String streamId, String outputId);
	
	public String createStreamTarget(StreamTarget streamTarget);
	
	public boolean addStreamTarget(String transcoderId, String outputId, String streamTargetId);

}
