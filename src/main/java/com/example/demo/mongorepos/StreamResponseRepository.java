package com.example.demo.mongorepos;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.IVSLiveStreamResponse;


@Repository
public interface StreamResponseRepository extends  MongoRepository<IVSLiveStreamResponse, String>{
	
}
