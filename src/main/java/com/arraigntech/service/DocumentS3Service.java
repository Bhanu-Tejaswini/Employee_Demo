package com.arraigntech.service;

import org.springframework.web.multipart.MultipartFile;

import com.arraigntech.exceptions.AppException;

public interface DocumentS3Service {
	
	String uploadFile(MultipartFile file) throws AppException;

	Boolean deleteFileFromS3Bucket(String filepath) throws AppException;
	
	Boolean saveAWSDocumentDetails(String documentType, String documentURL);

}
