/**
 * 
 */
package com.arraigntech.mongorepos;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.arraigntech.streams.model.StreamTargetDTO;

/**
 * @author Bhaskara S
 *
 */
public interface MongoStreamTargetRepository extends MongoRepository<StreamTargetDTO, String>{

}
