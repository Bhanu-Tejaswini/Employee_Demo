package com.arraigntech.mongorepos;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.arraigntech.streamsModel.IVSLiveStreamResponse;


public interface StreamResponseRepository extends  MongoRepository<IVSLiveStreamResponse, String>{
	
}
