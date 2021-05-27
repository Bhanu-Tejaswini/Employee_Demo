/**
 * 
 */
package com.arraigntech.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arraigntech.entity.StreamTarget;
import com.arraigntech.entity.Streams;

/**
 * @author Bhaskara S
 *
 */
public interface StreamTargetRepository extends JpaRepository<StreamTarget, String> {
	
	List<StreamTarget> findByStream(Streams stream);
	
	List<StreamTarget> findByCreatedAtBefore(Date date);
}
