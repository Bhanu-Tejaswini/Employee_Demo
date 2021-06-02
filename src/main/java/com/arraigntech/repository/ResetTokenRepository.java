package com.arraigntech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arraigntech.entity.ResetTokenEntity;
import com.arraigntech.entity.UserEntity;

public interface ResetTokenRepository extends JpaRepository<ResetTokenEntity, String> {
	
	ResetTokenEntity findByToken(String token);
	
	ResetTokenEntity findByUser(UserEntity newUser);

}
