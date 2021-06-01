package com.arraigntech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arraigntech.entity.ChannelEntity;
import com.arraigntech.entity.UpdateTitleEntity;

public interface UpdateTitleRepository extends JpaRepository<UpdateTitleEntity, String> {

	UpdateTitleEntity findByChannel(ChannelEntity channel);
}
