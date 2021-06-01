package com.arraigntech.model;

public class ChannelIngestionInfo {

	private String streamName;
	private String ingestionAddress;
	private String backupIngestionAddress;

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

	public String getIngestionAddress() {
		return ingestionAddress;
	}

	public void setIngestionAddress(String ingestionAddress) {
		this.ingestionAddress = ingestionAddress;
	}

	public String getBackupIngestionAddress() {
		return backupIngestionAddress;
	}

	public void setBackupIngestionAddress(String backupIngestionAddress) {
		this.backupIngestionAddress = backupIngestionAddress;
	}

	@Override
	public String toString() {
		return "ChannelIngestionInfo [streamName=" + streamName + ", ingestionAddress=" + ingestionAddress
				+ ", backupIngestionAddress=" + backupIngestionAddress + "]";
	}
}
