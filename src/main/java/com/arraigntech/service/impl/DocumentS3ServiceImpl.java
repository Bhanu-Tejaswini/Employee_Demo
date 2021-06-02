package com.arraigntech.service.impl;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.arraigntech.entity.AwsDocument;
import com.arraigntech.entity.UserEntity;
import com.arraigntech.exceptions.AppException;
import com.arraigntech.mongorepos.s3DocumentRepository;
import com.arraigntech.repository.UserRespository;
import com.arraigntech.response.vo.MongoUserVO;
import com.arraigntech.service.DocumentS3Service;
import com.arraigntech.utility.CommonUtils;
import com.arraigntech.utility.FileUtils;
import com.arraigntech.utility.MessageConstants;

@Service("documentS3Service")
public class DocumentS3ServiceImpl implements DocumentS3Service {

	public static final Logger LOGGER = LoggerFactory.getLogger(DocumentS3ServiceImpl.class);

	@Value("${document.s3storage.bucketName}")
	private String bucketName;

	@Value("${cloud.aws.credentials.accessKey}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secretKey}")
	private String skrtKey;

	@Value("${aws.region}")
	private String awsRegion;

	private TransferManager transferManager;

	private AmazonS3Client client;
	
	@Autowired
	private UserRespository userRepo;
	
	@Autowired
	private s3DocumentRepository documentRepository;

	@Override
	public String uploadFile(MultipartFile multipartFile) throws AppException {

		String documentPath = null;
		try {
			LOGGER.debug("uploadFile method started");
			File file = FileUtils.convertMultipartFileToFile(multipartFile);
			uploadFileToS3Bucket(bucketName, file);
			documentPath = client.getResourceUrl(bucketName, file.getName());
			LOGGER.debug("uploadFile method ended");
		} catch (Exception e) {
			LOGGER.error("Exception in uploading the file to S3", e);
		}

		return documentPath;
	}

	@Override
	public Boolean deleteFileFromS3Bucket(String filepath) throws AppException {
		try {
			LOGGER.debug("bucketName: " + bucketName + " file url :" + filepath);
			transferManager.getAmazonS3Client().deleteObject(bucketName, filepath);
		} catch (Exception e) {
			LOGGER.error("Exception in deleting the file from S3", e);
		}
		return true;
	}

	@PostConstruct
	private void initializeAmazon() throws NoSuchAlgorithmException {
		BasicAWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.skrtKey);
		this.client = (AmazonS3Client) AmazonS3ClientBuilder.standard().withRegion(this.awsRegion)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
		TransferManagerBuilder builder = TransferManagerBuilder.standard().withS3Client(client);
		transferManager = builder.build();
	}

	private void uploadFileToS3Bucket(String bucketName, File file) {
         String uniqueFileName = LocalDateTime.now() + "_" + file.getName();
        LOGGER.info("Uploading file with name= " + uniqueFileName);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file);
        client.putObject(putObjectRequest);
    }
	
	public Boolean saveAWSDocumentDetails(String documentType, String documentURL) {
		LOGGER.debug("Uploading file with name= " + documentType);
		UserEntity newUser=getUser();
		MongoUserVO user = new MongoUserVO(newUser.getId(),newUser.getEmail(),newUser.getUsername());
		AwsDocument document = new AwsDocument(bucketName, documentType, documentURL, user);
		documentRepository.save(document);
		return true;
	}
	
	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	private UserEntity getUser() {
		UserEntity user = userRepo.findByEmail(CommonUtils.getUser());
		if (Objects.isNull(user)) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		return user;
	}

}
