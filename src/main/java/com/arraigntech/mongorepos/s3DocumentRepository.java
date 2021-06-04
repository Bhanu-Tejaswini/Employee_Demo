package com.arraigntech.mongorepos;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.arraigntech.request.vo.AwsDocument;

public interface s3DocumentRepository extends MongoRepository<AwsDocument, String> {
	
	AwsDocument findByUser_EmailIgnoreCase(String email);

}
