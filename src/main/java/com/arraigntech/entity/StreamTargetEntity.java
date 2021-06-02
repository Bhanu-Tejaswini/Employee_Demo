/**
 * 
 */
package com.arraigntech.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Bhaskara S
 *
 */
@Entity
@Table(name = "stream_target")
public class StreamTargetEntity extends VSBaseModel {

	@Column(name = "stream_target_id")
	private String streamTargetId;

	@Column(name = "primary_url")
	private String primaryUrl;

	@Column(name = "stream_name")
	private String streamName;

	@Column(name = "backup_url")
	private String backupUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	private StreamEntity stream;
	
	public StreamTargetEntity() {
		
	}

	public String getStreamTargetId() {
		return streamTargetId;
	}

	public void setStreamTargetId(String streamTargetId) {
		this.streamTargetId = streamTargetId;
	}

	public String getPrimaryUrl() {
		return primaryUrl;
	}

	public void setPrimaryUrl(String primaryUrl) {
		this.primaryUrl = primaryUrl;
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

	public String getBackupUrl() {
		return backupUrl;
	}

	public void setBackupUrl(String backupUrl) {
		this.backupUrl = backupUrl;
	}

	public StreamEntity getStream() {
		return stream;
	}

	public void setStream(StreamEntity stream) {
		this.stream = stream;
	}

	public StreamTargetEntity(String streamTargetId, String primaryUrl, String streamName, String backupUrl, StreamEntity stream) {
		super();
		this.streamTargetId = streamTargetId;
		this.primaryUrl = primaryUrl;
		this.streamName = streamName;
		this.backupUrl = backupUrl;
		this.stream = stream;
	}

}
