package com.arraigntech.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arraigntech.entity.StreamEntity;

@Repository
public interface StreamRepository extends JpaRepository<StreamEntity, String> {
	
	StreamEntity findByStreamId(String streamId);
	
	List<StreamEntity> findByCreatedAtBefore(Date date);

}
