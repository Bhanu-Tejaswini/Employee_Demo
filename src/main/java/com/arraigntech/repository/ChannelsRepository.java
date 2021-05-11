package com.arraigntech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arraigntech.entity.Channels;
import com.arraigntech.entity.User;
import com.arraigntech.utility.ChannelTypeProvider;

public interface ChannelsRepository extends JpaRepository<Channels, String> {
	
	List<Channels> findByUser(User newUser);
	
	Channels findByChannelId(String id);
	
	List<Channels> findByUserAndType(User newUser,ChannelTypeProvider type);

}
