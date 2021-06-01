package com.arraigntech.mongorepos;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.arraigntech.wowza.response.vo.IVSLiveStreamResponseVO;


public interface MongoStreamResponseRepository extends  MongoRepository<IVSLiveStreamResponseVO, String>{
	
}
