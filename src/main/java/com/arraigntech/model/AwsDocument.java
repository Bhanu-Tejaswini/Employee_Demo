package com.arraigntech.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arraigntech.entity.VSBaseModel;

@Document(collection = "aws_document")
public class AwsDocument extends VSBaseModel{
	
	private String bucketName;
	
	private String documentType;
	
	private String documentURL;
	
	private MongoUser user;

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

	public MongoUser getUser() {
		return user;
	}

	public void setUser(MongoUser user) {
		this.user = user;
	}

	public AwsDocument(String bucketName, String documentType, String documentURL, MongoUser user) {
		super();
		this.bucketName = bucketName;
		this.documentType = documentType;
		this.documentURL = documentURL;
		this.user = user;
	}

}
