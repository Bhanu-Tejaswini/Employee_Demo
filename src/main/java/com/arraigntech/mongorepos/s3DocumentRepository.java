package com.arraigntech.mongorepos;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.arraigntech.entity.AwsDocument;

public interface s3DocumentRepository extends MongoRepository<AwsDocument, String>  {

}
