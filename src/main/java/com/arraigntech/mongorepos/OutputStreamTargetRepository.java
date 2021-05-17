package com.arraigntech.mongorepos;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.arraigntech.streamsModel.OutputStreamTargetDTO;

/**
 * @author Bhaskara S
 *
 */
public interface OutputStreamTargetRepository extends MongoRepository<OutputStreamTargetDTO,String> {

}
