package com.arraigntech.mongorepos;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.arraigntech.model.IVSLiveStreamResponse;


public interface StreamResponseRepository extends  MongoRepository<IVSLiveStreamResponse, String>{
	
}
