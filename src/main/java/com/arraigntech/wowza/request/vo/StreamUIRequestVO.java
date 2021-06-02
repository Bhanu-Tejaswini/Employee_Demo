/**
 * 
 */
package com.arraigntech.wowza.request.vo;

/**
 * @author Bhaskara S
 *
 */
public class StreamUIRequestVO {
	private int aspectRatioHeight;
	private int aspectRatioWidth;

	public int getAspectRatioHeight() {
		return aspectRatioHeight;
	}

	public void setAspectRatioHeight(int aspectRatioHeight) {
		this.aspectRatioHeight = aspectRatioHeight;
	}

	public int getAspectRatioWidth() {
		return aspectRatioWidth;
	}

	public void setAspectRatioWidth(int aspectRatioWidth) {
		this.aspectRatioWidth = aspectRatioWidth;
	}

	@Override
	public String toString() {
		return "StreamUIRequest [aspectRatioHeight=" + aspectRatioHeight + ", aspectRatioWidth=" + aspectRatioWidth
				+ "]";
	}
}
