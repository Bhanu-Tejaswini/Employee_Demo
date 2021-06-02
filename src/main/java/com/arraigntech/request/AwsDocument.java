package com.arraigntech.request;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arraigntech.entity.VSBaseModel;
import com.arraigntech.response.MongoUserVO;

@Document(collection = "aws_document")
public class AwsDocument extends VSBaseModel{
	
	private String bucketName;
	
	private String documentType;
	
	private String documentURL;
	
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

	public AwsDocument(String bucketName, String documentType, String documentURL, MongoUserVO user) {
		super();
		this.bucketName = bucketName;
		this.documentType = documentType;
		this.documentURL = documentURL;
		this.user = user;
	}

}
