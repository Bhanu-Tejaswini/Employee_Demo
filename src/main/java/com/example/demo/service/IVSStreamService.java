package com.example.demo.service;

import com.example.demo.model.IVSLiveStream;
import com.example.demo.model.IVSLiveStreamResponse;
import com.example.demo.model.LiveStreamState;

/**
 * 
 * @author tulabandula.kumar
 *
 */

public interface IVSStreamService {

	public IVSLiveStreamResponse createStream(IVSLiveStream liveStream);
	
	public String startStream(String id);
	
	public String stopStream(String id);
	
	public LiveStreamState fetchStreamState(String id);
	
	public void saveStream(IVSLiveStreamResponse response);
}
