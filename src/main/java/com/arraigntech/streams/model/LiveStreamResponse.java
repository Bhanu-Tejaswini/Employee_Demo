package com.arraigntech.streams.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LiveStreamResponse {

	private float aspect_ratio_height;
	private float aspect_ratio_width;
	private String billing_mode;
	private String broadcast_location;
	private String closed_caption_type;
	private String connection_code;
	private String connection_code_expires_at;
	private String created_at;
	private String delivery_method;
	ArrayList<Object> delivery_protocols = new ArrayList<Object>();
	private String delivery_type;
	private StreamSourceConnectionInformation source_connection_information;
	@JsonProperty("direct_playback_urls")
	private DirectPlaybackUrls direct_playback_urls;
	private String encoder;
	private boolean hosted_page;
	private String hosted_page_description;
	private String hosted_page_logo_image_url;
	private boolean hosted_page_sharing_icons;
	private String hosted_page_title;
	private String hosted_page_url;
	private String id;
	private boolean low_latency;
	private String name;
	private String playback_stream_name;
	private boolean player_countdown;
	private String player_countdown_at;
	private String player_embed_code = null;
	private String player_hls_playback_url;
	private String player_id;
	private String player_logo_image_url;
	private String player_logo_position;
	private boolean player_responsive;
	private String player_type;
	private String player_video_poster_image_url;
	private float player_width;
	private boolean recording;
	private String stream_source_id;
	ArrayList<Object> stream_targets = new ArrayList<Object>();
	private String target_delivery_protocol;
	private String transcoder_type;
	private String updated_at;
	private boolean use_stream_source;
	private boolean vod_stream;

	// Getter Methods

	public float getAspect_ratio_height() {
		return aspect_ratio_height;
	}

	public float getAspect_ratio_width() {
		return aspect_ratio_width;
	}

	public String getBilling_mode() {
		return billing_mode;
	}

	public String getBroadcast_location() {
		return broadcast_location;
	}

	public String getClosed_caption_type() {
		return closed_caption_type;
	}

	public String getConnection_code() {
		return connection_code;
	}

	public String getConnection_code_expires_at() {
		return connection_code_expires_at;
	}

	public String getCreated_at() {
		return created_at;
	}

	public String getDelivery_method() {
		return delivery_method;
	}

	public String getDelivery_type() {
		return delivery_type;
	}

	public ArrayList<Object> getDelivery_protocols() {
		return delivery_protocols;
	}

	public void setDelivery_protocols(ArrayList<Object> delivery_protocols) {
		this.delivery_protocols = delivery_protocols;
	}

	public DirectPlaybackUrls getDirect_playback_urls() {
		return direct_playback_urls;
	}

	public void setDirect_playback_urls(DirectPlaybackUrls direct_playback_urls) {
		this.direct_playback_urls = direct_playback_urls;
	}

	public ArrayList<Object> getStream_targets() {
		return stream_targets;
	}

	public void setStream_targets(ArrayList<Object> stream_targets) {
		this.stream_targets = stream_targets;
	}

	public String getEncoder() {
		return encoder;
	}

	public boolean getHosted_page() {
		return hosted_page;
	}

	public String getHosted_page_description() {
		return hosted_page_description;
	}

	public String getHosted_page_logo_image_url() {
		return hosted_page_logo_image_url;
	}

	public boolean getHosted_page_sharing_icons() {
		return hosted_page_sharing_icons;
	}

	public String getHosted_page_title() {
		return hosted_page_title;
	}

	public String getHosted_page_url() {
		return hosted_page_url;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPlayback_stream_name() {
		return playback_stream_name;
	}

	public boolean getPlayer_countdown() {
		return player_countdown;
	}

	public String getPlayer_countdown_at() {
		return player_countdown_at;
	}

	public String getPlayer_embed_code() {
		return player_embed_code;
	}

	public String getPlayer_hls_playback_url() {
		return player_hls_playback_url;
	}

	public String getPlayer_id() {
		return player_id;
	}

	public String getPlayer_logo_image_url() {
		return player_logo_image_url;
	}

	public String getPlayer_logo_position() {
		return player_logo_position;
	}

	public boolean getPlayer_responsive() {
		return player_responsive;
	}

	public String getPlayer_type() {
		return player_type;
	}

	public String getPlayer_video_poster_image_url() {
		return player_video_poster_image_url;
	}

	public float getPlayer_width() {
		return player_width;
	}

	public boolean getRecording() {
		return recording;
	}

	public String getStream_source_id() {
		return stream_source_id;
	}

	public String getTarget_delivery_protocol() {
		return target_delivery_protocol;
	}

	public String getTranscoder_type() {
		return transcoder_type;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public boolean getUse_stream_source() {
		return use_stream_source;
	}

	public boolean getVod_stream() {
		return vod_stream;
	}

	// Setter Methods

	public void setAspect_ratio_height(float aspect_ratio_height) {
		this.aspect_ratio_height = aspect_ratio_height;
	}

	public void setAspect_ratio_width(float aspect_ratio_width) {
		this.aspect_ratio_width = aspect_ratio_width;
	}

	public void setBilling_mode(String billing_mode) {
		this.billing_mode = billing_mode;
	}

	public void setBroadcast_location(String broadcast_location) {
		this.broadcast_location = broadcast_location;
	}

	public void setClosed_caption_type(String closed_caption_type) {
		this.closed_caption_type = closed_caption_type;
	}

	public void setConnection_code(String connection_code) {
		this.connection_code = connection_code;
	}

	public void setConnection_code_expires_at(String connection_code_expires_at) {
		this.connection_code_expires_at = connection_code_expires_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public void setDelivery_method(String delivery_method) {
		this.delivery_method = delivery_method;
	}

	public void setDelivery_type(String delivery_type) {
		this.delivery_type = delivery_type;
	}

	public void setEncoder(String encoder) {
		this.encoder = encoder;
	}

	public void setHosted_page(boolean hosted_page) {
		this.hosted_page = hosted_page;
	}

	public void setHosted_page_description(String hosted_page_description) {
		this.hosted_page_description = hosted_page_description;
	}

	public void setHosted_page_logo_image_url(String hosted_page_logo_image_url) {
		this.hosted_page_logo_image_url = hosted_page_logo_image_url;
	}

	public void setHosted_page_sharing_icons(boolean hosted_page_sharing_icons) {
		this.hosted_page_sharing_icons = hosted_page_sharing_icons;
	}

	public void setHosted_page_title(String hosted_page_title) {
		this.hosted_page_title = hosted_page_title;
	}

	public void setHosted_page_url(String hosted_page_url) {
		this.hosted_page_url = hosted_page_url;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPlayback_stream_name(String playback_stream_name) {
		this.playback_stream_name = playback_stream_name;
	}

	public void setPlayer_countdown(boolean player_countdown) {
		this.player_countdown = player_countdown;
	}

	public void setPlayer_countdown_at(String player_countdown_at) {
		this.player_countdown_at = player_countdown_at;
	}

	public void setPlayer_embed_code(String player_embed_code) {
		this.player_embed_code = player_embed_code;
	}

	public void setPlayer_hls_playback_url(String player_hls_playback_url) {
		this.player_hls_playback_url = player_hls_playback_url;
	}

	public void setPlayer_id(String player_id) {
		this.player_id = player_id;
	}

	public void setPlayer_logo_image_url(String player_logo_image_url) {
		this.player_logo_image_url = player_logo_image_url;
	}

	public void setPlayer_logo_position(String player_logo_position) {
		this.player_logo_position = player_logo_position;
	}

	public void setPlayer_responsive(boolean player_responsive) {
		this.player_responsive = player_responsive;
	}

	public void setPlayer_type(String player_type) {
		this.player_type = player_type;
	}

	public void setPlayer_video_poster_image_url(String player_video_poster_image_url) {
		this.player_video_poster_image_url = player_video_poster_image_url;
	}

	public void setPlayer_width(float player_width) {
		this.player_width = player_width;
	}

	public void setRecording(boolean recording) {
		this.recording = recording;
	}

	public void setStream_source_id(String stream_source_id) {
		this.stream_source_id = stream_source_id;
	}

	public void setTarget_delivery_protocol(String target_delivery_protocol) {
		this.target_delivery_protocol = target_delivery_protocol;
	}

	public void setTranscoder_type(String transcoder_type) {
		this.transcoder_type = transcoder_type;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public void setUse_stream_source(boolean use_stream_source) {
		this.use_stream_source = use_stream_source;
	}

	public void setVod_stream(boolean vod_stream) {
		this.vod_stream = vod_stream;
	}

	public boolean isLow_latency() {
		return low_latency;
	}

	public void setLow_latency(boolean low_latency) {
		this.low_latency = low_latency;
	}

	public StreamSourceConnectionInformation getSource_connection_information() {
		return source_connection_information;
	}

	public void setSource_connection_information(StreamSourceConnectionInformation source_connection_information) {
		this.source_connection_information = source_connection_information;
	}

	@Override
	public String toString() {
		return "LiveStreamResponse [aspect_ratio_height=" + aspect_ratio_height + ", aspect_ratio_width="
				+ aspect_ratio_width + ", billing_mode=" + billing_mode + ", broadcast_location=" + broadcast_location
				+ ", closed_caption_type=" + closed_caption_type + ", connection_code=" + connection_code
				+ ", connection_code_expires_at=" + connection_code_expires_at + ", created_at=" + created_at
				+ ", delivery_method=" + delivery_method + ", delivery_protocols=" + delivery_protocols
				+ ", delivery_type=" + delivery_type + ", source_connection_information="
				+ source_connection_information + ", direct_playback_urls=" + direct_playback_urls + ", encoder="
				+ encoder + ", hosted_page=" + hosted_page + ", hosted_page_description=" + hosted_page_description
				+ ", hosted_page_logo_image_url=" + hosted_page_logo_image_url + ", hosted_page_sharing_icons="
				+ hosted_page_sharing_icons + ", hosted_page_title=" + hosted_page_title + ", hosted_page_url="
				+ hosted_page_url + ", id=" + id + ", low_latency=" + low_latency + ", name=" + name
				+ ", playback_stream_name=" + playback_stream_name + ", player_countdown=" + player_countdown
				+ ", player_countdown_at=" + player_countdown_at + ", player_embed_code=" + player_embed_code
				+ ", player_hls_playback_url=" + player_hls_playback_url + ", player_id=" + player_id
				+ ", player_logo_image_url=" + player_logo_image_url + ", player_logo_position=" + player_logo_position
				+ ", player_responsive=" + player_responsive + ", player_type=" + player_type
				+ ", player_video_poster_image_url=" + player_video_poster_image_url + ", player_width=" + player_width
				+ ", recording=" + recording + ", stream_source_id=" + stream_source_id + ", stream_targets="
				+ stream_targets + ", target_delivery_protocol=" + target_delivery_protocol + ", transcoder_type="
				+ transcoder_type + ", updated_at=" + updated_at + ", use_stream_source=" + use_stream_source
				+ ", vod_stream=" + vod_stream + "]";
	}
}
