/**
 * 
 */
package com.arraigntech.mongorepos;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.arraigntech.wowza.response.vo.StreamTargetRootVO;

/**
 * @author Bhaskara S
 *
 */
public interface MongoStreamTargetRepository extends MongoRepository<StreamTargetRootVO, String>{

}
