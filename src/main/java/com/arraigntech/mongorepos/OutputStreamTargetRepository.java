package com.arraigntech.mongorepos;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.arraigntech.wowza.response.vo.OutputStreamTargetRootVO;

/**
 * @author Bhaskara S
 *
 */
public interface OutputStreamTargetRepository extends MongoRepository<OutputStreamTargetRootVO,String> {

}
