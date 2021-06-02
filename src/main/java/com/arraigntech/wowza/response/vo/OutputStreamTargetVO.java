package com.arraigntech.wowza.response.vo;


public class OutputStreamTargetVO {

	private String id;
	private String stream_target_id;
	private boolean use_stream_target_backup_url;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStream_target_id() {
		return stream_target_id;
	}

	public void setStream_target_id(String stream_target_id) {
		this.stream_target_id = stream_target_id;
	}

	public boolean isUse_stream_target_backup_url() {
		return use_stream_target_backup_url;
	}

	public void setUse_stream_target_backup_url(boolean use_stream_target_backup_url) {
		this.use_stream_target_backup_url = use_stream_target_backup_url;
	}

	public OutputStreamTargetVO(String stream_target_id, boolean use_stream_target_backup_url) {
		super();
		this.stream_target_id = stream_target_id;
		this.use_stream_target_backup_url = use_stream_target_backup_url;
	}

	public OutputStreamTargetVO() {}

	@Override
	public String toString() {
		return "OutputStreamTarget [id=" + id + ", stream_target_id=" + stream_target_id
				+ ", use_stream_target_backup_url=" + use_stream_target_backup_url + "]";
	}
}