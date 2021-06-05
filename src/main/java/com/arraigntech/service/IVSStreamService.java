package com.arraigntech.service;

import java.util.List;

import com.arraigntech.entity.ChannelEntity;
import com.arraigntech.wowza.request.vo.StreamUIRequestVO;
import com.arraigntech.wowza.response.vo.FetchStreamUIResponseVO;
import com.arraigntech.wowza.response.vo.IVSLiveStreamResponseVO;
import com.arraigntech.wowza.response.vo.StreamTargetVO;
import com.arraigntech.wowza.response.vo.StreamUIResponseVO;
import com.arraigntech.wowza.response.vo.WebrtcVO;

public interface IVSStreamService {

	public StreamUIResponseVO createStream(StreamUIRequestVO streamRequest);

	public void startStream(String id);

	public String stopStream(String id);

	public boolean deleteStream(String streamId);

	public FetchStreamUIResponseVO fetchStreamState(String id);

	public void saveStream(IVSLiveStreamResponseVO response);

	public void youtubeStream(List<ChannelEntity> youtubeChannels, String streamId, List<WebrtcVO> webrtcList);

	public String createStreamTarget(StreamTargetVO streamTarget, String streamId);

	public boolean deleteStreamTarget(String streamId);

	public boolean addStreamTarget(String transcoderId, String outputId, String streamTargetId, boolean addBackupUrl);

}
