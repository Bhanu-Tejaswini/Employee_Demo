package com.arraigntech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arraigntech.entity.EmailSettingEntity;
import com.arraigntech.entity.UserEntity;

public interface EmailSettingsRepository extends JpaRepository<EmailSettingEntity,String> {
	
	EmailSettingEntity findByUser(UserEntity user);

}
