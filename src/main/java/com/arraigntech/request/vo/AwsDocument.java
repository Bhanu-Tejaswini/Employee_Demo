package com.arraigntech.request.vo;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arraigntech.entity.VSBaseModel;
import com.arraigntech.response.vo.MongoUserVO;

@Document(collection = "aws_document")
public class AwsDocument extends VSBaseModel {

	private String bucketName;

	private String documentType;

	private String documentURL;

	private boolean active;

	private MongoUserVO user;

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentURL() {
		return documentURL;
	}

	public void setDocumentURL(String documentURL) {
		this.documentURL = documentURL;
	}

	public MongoUserVO getUser() {
		return user;
	}

	public void setUser(MongoUserVO user) {
		this.user = user;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public AwsDocument(String bucketName, String documentType, String documentURL, boolean active, MongoUserVO user) {
		super();
		this.bucketName = bucketName;
		this.documentType = documentType;
		this.documentURL = documentURL;
		this.active = active;
		this.user = user;
	}

	public AwsDocument() {

	}

}
