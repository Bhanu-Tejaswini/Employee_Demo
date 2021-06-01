package com.arraigntech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arraigntech.entity.ChannelEntity;
import com.arraigntech.entity.UserEntity;
import com.arraigntech.utility.ChannelTypeProvider;

public interface ChannelsRepository extends JpaRepository<ChannelEntity, String> {
	
	List<ChannelEntity> findByUser(UserEntity newUser);
	
	ChannelEntity findByChannelId(String id);
	
	List<ChannelEntity> findByUserAndType(UserEntity newUser,ChannelTypeProvider type);
	
	ChannelEntity findByFacebookUserId(String userId);
	
	List<ChannelEntity> findByUserAndTypeAndActive(UserEntity newUser, ChannelTypeProvider type,boolean active);

}
