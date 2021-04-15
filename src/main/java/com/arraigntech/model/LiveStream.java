package com.arraigntech.model;

public class LiveStream {
	public int aspect_ratio_height;
	public int aspect_ratio_width;
	public String billing_mode;
	public String broadcast_location;
	public String encoder;
	public String name;
	public String transcoder_type;
	public String closed_caption_type;
	public String delivery_method;
//	public List<String> delivery_protocols;
	public String delivery_type;
	public boolean disable_authentication;
//	public boolean hosted_page;
//	public String hosted_page_description;
//	public String hosted_page_logo_image;
//	public boolean hosted_page_sharing_icons;
//	public String hosted_page_title;
//	public boolean low_latency;
//	public String password;
//	public boolean player_countdown;
//	public Date player_countdown_at;
//	public String player_logo_image;
	public String player_logo_position;
	public boolean player_responsive;
//	public String player_type;
//	public String player_video_poster_image;
//	public int player_width;
	public boolean recording;
//	public boolean remove_hosted_page_logo_image;
//	public boolean remove_player_logo_image;
//	public boolean remove_player_video_poster_image;
//	public String source_url;
//	public String target_delivery_protocol;
//	public boolean use_stream_source;
	public String username;

//	public boolean vod_stream;
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

	public String getClosed_caption_type() {
		return closed_caption_type;
	}

	public void setClosed_caption_type(String closed_caption_type) {
		this.closed_caption_type = closed_caption_type;
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

	public boolean isDisable_authentication() {
		return disable_authentication;
	}

	public void setDisable_authentication(boolean disable_authentication) {
		this.disable_authentication = disable_authentication;
	}

	public String getPlayer_logo_position() {
		return player_logo_position;
	}

	public void setPlayer_logo_position(String player_logo_position) {
		this.player_logo_position = player_logo_position;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
