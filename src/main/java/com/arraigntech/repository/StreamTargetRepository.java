/**
 * 
 */
package com.arraigntech.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arraigntech.entity.StreamTargetEntity;
import com.arraigntech.entity.StreamEntity;

/**
 * @author Bhaskara S
 *
 */
public interface StreamTargetRepository extends JpaRepository<StreamTargetEntity, String> {
	
	List<StreamTargetEntity> findByStream(StreamEntity stream);
	
	List<StreamTargetEntity> findByCreatedAtBefore(Date date);
}
