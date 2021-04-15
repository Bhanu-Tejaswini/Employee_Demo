package com.example.demo.mongorepos;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.IVSLiveStreamResponse;


public interface StreamResponseRepository extends  MongoRepository<IVSLiveStreamResponse, String>{
	
}
