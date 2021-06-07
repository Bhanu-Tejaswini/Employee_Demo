package com.arraigntech.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.arraigntech.exceptions.AppException;
import com.arraigntech.response.vo.S3UIResponse;

public interface DocumentS3Service {
	
	String uploadFile(MultipartFile file) throws AppException;

	public Boolean deleteFileFromS3Bucket(String id);
	
	Map<String, String> saveAWSDocumentDetails(String documentType, String documentURL);

	S3UIResponse getDocumentImageURL();
	

}
