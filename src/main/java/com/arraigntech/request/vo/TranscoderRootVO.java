/**
 * 
 */
package com.arraigntech.request.vo;

/**
 * @author Bhaskara S
 *
 */
public class TranscoderRootVO {
	private TranscoderVO transcoder;

	public TranscoderVO getTranscoder() {
		return transcoder;
	}

	public void setTranscoder(TranscoderVO transcoder) {
		this.transcoder = transcoder;
	}

	public TranscoderRootVO(TranscoderVO transcoder) {
		super();
		this.transcoder = transcoder;
	}

	public TranscoderRootVO() {

	}

}
