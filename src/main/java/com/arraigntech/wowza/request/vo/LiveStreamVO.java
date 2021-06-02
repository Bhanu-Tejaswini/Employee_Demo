package com.arraigntech.wowza.request.vo;

/**
 * @author Bhaskara S
 *
 */
public class LiveStreamVO {
	public int aspect_ratio_height;
	public int aspect_ratio_width;
	public String billing_mode;
	public String broadcast_location;
	public String encoder;
	public String name;
	public String transcoder_type;
	public String delivery_method;
	public String delivery_type;
	public boolean player_responsive;
	public boolean recording;

	public int getAspect_ratio_height() {
		return aspect_ratio_height;
	}

	public void setAspect_ratio_height(int aspect_ratio_height) {
		this.aspect_ratio_height = aspect_ratio_height;
	}

	public int getAspect_ratio_width() {
		return aspect_ratio_width;
	}

	public void setAspect_ratio_width(int aspect_ratio_width) {
		this.aspect_ratio_width = aspect_ratio_width;
	}

	public String getBilling_mode() {
		return billing_mode;
	}

	public void setBilling_mode(String billing_mode) {
		this.billing_mode = billing_mode;
	}

	public String getBroadcast_location() {
		return broadcast_location;
	}

	public void setBroadcast_location(String broadcast_location) {
		this.broadcast_location = broadcast_location;
	}

	public String getEncoder() {
		return encoder;
	}

	public void setEncoder(String encoder) {
		this.encoder = encoder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTranscoder_type() {
		return transcoder_type;
	}

	public void setTranscoder_type(String transcoder_type) {
		this.transcoder_type = transcoder_type;
	}

	public String getDelivery_method() {
		return delivery_method;
	}

	public void setDelivery_method(String delivery_method) {
		this.delivery_method = delivery_method;
	}

	public String getDelivery_type() {
		return delivery_type;
	}

	public void setDelivery_type(String delivery_type) {
		this.delivery_type = delivery_type;
	}

	public boolean isPlayer_responsive() {
		return player_responsive;
	}

	public void setPlayer_responsive(boolean player_responsive) {
		this.player_responsive = player_responsive;
	}

	public boolean isRecording() {
		return recording;
	}

	public void setRecording(boolean recording) {
		this.recording = recording;
	}

	@Override
	public String toString() {
		return "LiveStream [aspect_ratio_height=" + aspect_ratio_height + ", aspect_ratio_width=" + aspect_ratio_width
				+ ", billing_mode=" + billing_mode + ", broadcast_location=" + broadcast_location + ", encoder="
				+ encoder + ", name=" + name + ", transcoder_type=" + transcoder_type + ", delivery_method="
				+ delivery_method + ", delivery_type=" + delivery_type + ", player_responsive=" + player_responsive
				+ ", recording=" + recording + "]";
	}
}
