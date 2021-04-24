package com.arraigntech.service;

import com.arraigntech.streamsModel.IVSLiveStream;
import com.arraigntech.streamsModel.IVSLiveStreamResponse;
import com.arraigntech.streamsModel.LiveStreamState;

public interface IVSStreamService {

	public IVSLiveStreamResponse createStream(IVSLiveStream liveStream);
	
	public String startStream(String id);
	
	public String stopStream(String id);
	
	public LiveStreamState fetchStreamState(String id);
	
	public void saveStream(IVSLiveStreamResponse response);

}
