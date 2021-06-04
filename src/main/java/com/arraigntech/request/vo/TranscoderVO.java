/**
 * 
 */
package com.arraigntech.request.vo;

/**
 * @author Bhaskara S
 *
 */
public class TranscoderVO {
	private boolean watermark;
	private int watermark_height;
	private String watermark_image;
	private int watermark_opacity;
	private String watermark_position;
	private int watermark_width;

	public boolean isWatermark() {
		return watermark;
	}

	public void setWatermark(boolean watermark) {
		this.watermark = watermark;
	}

	public int getWatermark_height() {
		return watermark_height;
	}

	public void setWatermark_height(int watermark_height) {
		this.watermark_height = watermark_height;
	}

	public String getWatermark_image() {
		return watermark_image;
	}

	public void setWatermark_image(String watermark_image) {
		this.watermark_image = watermark_image;
	}

	public int getWatermark_opacity() {
		return watermark_opacity;
	}

	public void setWatermark_opacity(int watermark_opacity) {
		this.watermark_opacity = watermark_opacity;
	}

	public String getWatermark_position() {
		return watermark_position;
	}

	public void setWatermark_position(String watermark_position) {
		this.watermark_position = watermark_position;
	}

	public int getWatermark_width() {
		return watermark_width;
	}

	public void setWatermark_width(int watermark_width) {
		this.watermark_width = watermark_width;
	}

	public TranscoderVO(boolean watermark, int watermark_height, String watermark_image, int watermark_opacity,
			String watermark_position, int watermark_width) {
		super();
		this.watermark = watermark;
		this.watermark_height = watermark_height;
		this.watermark_image = watermark_image;
		this.watermark_opacity = watermark_opacity;
		this.watermark_position = watermark_position;
		this.watermark_width = watermark_width;
	}

	public TranscoderVO() {

	}

	@Override
	public String toString() {
		return "TranscoderVO [watermark=" + watermark + ", watermark_height=" + watermark_height + ", watermark_image="
				+ watermark_image + ", watermark_opacity=" + watermark_opacity + ", watermark_position="
				+ watermark_position + ", watermark_width=" + watermark_width + "]";
	}

}
