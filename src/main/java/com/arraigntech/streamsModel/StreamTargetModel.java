package com.arraigntech.streamsModel;

import org.springframework.data.mongodb.core.mapping.Document;

public class StreamTargetModel {

	private String id;
	private String name;
	private String provider;
	private String primary_url;
	private String stream_name;
	private String backup_url;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getPrimary_url() {
		return primary_url;
	}

	public void setPrimary_url(String primary_url) {
		this.primary_url = primary_url;
	}

	public String getStream_name() {
		return stream_name;
	}

	public void setStream_name(String stream_name) {
		this.stream_name = stream_name;
	}

	public String getBackup_url() {
		return backup_url;
	}

	public void setBackup_url(String backup_url) {
		this.backup_url = backup_url;
	}

	public StreamTargetModel(String name, String provider, String primary_url, String stream_name) {
		super();
		this.name = name;
		this.provider = provider;
		this.primary_url = primary_url;
		this.stream_name = stream_name;
	}

	public StreamTargetModel(String name, String provider, String primary_url, String stream_name,
			String backup_url) {
		super();

		this.name = name;
		this.provider = provider;
		this.primary_url = primary_url;
		this.stream_name = stream_name;
		this.backup_url = backup_url;
	}

	public StreamTargetModel() {

	}

}
