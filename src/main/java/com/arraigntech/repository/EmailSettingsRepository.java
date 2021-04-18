package com.arraigntech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arraigntech.entity.EmailSettings;
import com.arraigntech.entity.User;

public interface EmailSettingsRepository extends JpaRepository<EmailSettings,String> {
	
	EmailSettings findByUser(User user);

}
