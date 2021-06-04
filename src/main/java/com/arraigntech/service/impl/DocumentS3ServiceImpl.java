package com.arraigntech.service.impl;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.arraigntech.entity.UserEntity;
import com.arraigntech.exceptions.AppException;
import com.arraigntech.mongorepos.s3DocumentRepository;
import com.arraigntech.request.vo.AwsDocument;
import com.arraigntech.response.vo.MongoUserVO;
import com.arraigntech.response.vo.S3UIResponse;
import com.arraigntech.service.DocumentS3Service;
import com.arraigntech.service.UserService;
import com.arraigntech.utility.FileUtils;
import com.arraigntech.utility.MessageConstants;

@Service("documentS3Service")
public class DocumentS3ServiceImpl implements DocumentS3Service {

	public static final Logger LOGGER = LoggerFactory.getLogger(DocumentS3ServiceImpl.class);

	public static final String VSTREEM_IMAGE = "https://vstreem-images.s3.us-east-2.amazonaws.com/app-default-logo/default-vstreem.png";
	public static final String BRANDLOG_S3_FOLDER_NAME = "brandlogo/";

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
	private UserService userService;

	@Autowired
	private s3DocumentRepository documentRepository;

	@Override
	public String uploadFile(MultipartFile multipartFile) throws AppException {

		String documentPath = null;
		String fileName = null;
		try {
			LOGGER.debug("uploadFile method started");
			File file = FileUtils.convertMultipartFileToFile(multipartFile);
			fileName = uploadFileToS3Bucket(bucketName, file);
			documentPath = client.getResourceUrl(bucketName,BRANDLOG_S3_FOLDER_NAME+fileName);
			LOGGER.debug("uploadFile method ended");
		} catch (Exception e) {
			LOGGER.error("Exception in uploading the file to S3", e);
		}
		return documentPath;
	}

	@Override
	public Boolean deleteFileFromS3Bucket(String id) {
		if (!StringUtils.hasText(id)) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		Optional<AwsDocument> document = documentRepository.findById(id);
		if (Objects.isNull(document)) {
			throw new AppException(MessageConstants.INVALID_REQUEST);
		}
		String fileName = document.get().getDocumentURL().substring(document.get().getDocumentURL().lastIndexOf("/") + 1);
		try {
			LOGGER.debug("bucketName: " + bucketName + " file url :" + bucketName);
			ObjectListing objects = client.listObjects(bucketName, BRANDLOG_S3_FOLDER_NAME);
			for(S3ObjectSummary os : objects.getObjectSummaries()) {
				if(os.getKey().contains(fileName)) {
					fileName = os.getKey();
				}
			}
			client.deleteObject(bucketName, fileName);
		} catch (Exception e) {
			LOGGER.error("Exception in deleting the file from S3", e);
		}
		documentRepository.deleteById(id);
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

	private String uploadFileToS3Bucket(String bucketName, File file) {
		String uniqueFileName = file.getName();
		LOGGER.info("Uploading file with name= " + uniqueFileName);
		client.putObject(new PutObjectRequest(bucketName, BRANDLOG_S3_FOLDER_NAME+uniqueFileName, file)
				.withCannedAcl(CannedAccessControlList.PublicRead));
		return uniqueFileName;
	}

	public Boolean saveAWSDocumentDetails(String documentType, String documentURL) {
		LOGGER.debug("Uploading file with name= " + documentType);
		UserEntity newUser = userService.getUser();
		AwsDocument document = documentRepository.findByUser_EmailIgnoreCase(newUser.getEmail());
		if (Objects.isNull(document)) {
			MongoUserVO user = new MongoUserVO(newUser.getId(), newUser.getEmail(), newUser.getUsername());
			document = new AwsDocument(bucketName, documentType, documentURL, user);
		} else {
			document.setDocumentType(documentType);
			document.setDocumentURL(documentURL);
		}
		documentRepository.save(document);
		return true;
	}

	public S3UIResponse getDocumentImageURL() {
		UserEntity newUser = userService.getUser();
		AwsDocument document = documentRepository.findByUser_EmailIgnoreCase(newUser.getEmail());
		if (Objects.nonNull(document) && Objects.nonNull(document.getDocumentURL())) {
			return new S3UIResponse(document.getDocumentURL(), document.getId());
		} else {
			return new S3UIResponse(VSTREEM_IMAGE);
		}
	}
}
