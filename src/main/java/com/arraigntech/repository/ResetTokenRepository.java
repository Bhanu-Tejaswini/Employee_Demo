package com.arraigntech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arraigntech.entity.ResetToken;
import com.arraigntech.entity.User;

public interface ResetTokenRepository extends JpaRepository<ResetToken, String> {
	
	ResetToken findByToken(String token);
	
	ResetToken findByUser(User newUser);

}
