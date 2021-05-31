package com.arraigntech.mongorepos;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.arraigntech.streams.model.OutputStreamTargetDTO;

/**
 * @author Bhaskara S
 *
 */
public interface OutputStreamTargetRepository extends MongoRepository<OutputStreamTargetDTO,String> {

}
