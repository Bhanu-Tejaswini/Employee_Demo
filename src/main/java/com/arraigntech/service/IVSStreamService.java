package com.arraigntech.service;

import com.arraigntech.model.IVSLiveStream;
import com.arraigntech.model.IVSLiveStreamResponse;
import com.arraigntech.model.LiveStreamState;

public interface IVSStreamService {

	public IVSLiveStreamResponse createStream(IVSLiveStream liveStream);
	
	public String startStream(String id);
	
	public String stopStream(String id);
	
	public LiveStreamState fetchStreamState(String id);
	
	public void saveStream(IVSLiveStreamResponse response);

}
