package com.arraigntech.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arraigntech.entity.Streams;

@Repository
public interface StreamRepository extends JpaRepository<Streams, String> {
	
	Streams findByStreamId(String streamId);
	
	List<Streams> findByCreatedAtBefore(Date date);

}
