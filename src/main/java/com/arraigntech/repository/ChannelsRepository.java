package com.arraigntech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arraigntech.entity.Channels;
import com.arraigntech.entity.User;
import com.arraigntech.utility.AuthenticationProvider;

public interface ChannelsRepository extends JpaRepository<Channels, String> {
	
	Channels findByAccount(AuthenticationProvider account);
	
	List<Channels> findByUser(User newUser);
	
	Channels findByChannelId(String id);

}
