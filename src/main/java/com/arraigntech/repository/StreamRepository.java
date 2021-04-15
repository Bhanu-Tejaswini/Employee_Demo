package com.arraigntech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arraigntech.entity.Streams;

@Repository
public interface StreamRepository extends JpaRepository<Streams, String> {
	
	Streams findByStreamId(String id);

}
