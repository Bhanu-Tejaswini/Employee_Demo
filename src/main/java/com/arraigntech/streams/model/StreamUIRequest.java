/**
 * 
 */
package com.arraigntech.streams.model;

/**
 * @author Bhaskara S
 *
 */
public class StreamUIRequest {
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
