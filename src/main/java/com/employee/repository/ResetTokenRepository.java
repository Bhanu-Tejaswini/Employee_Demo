package com.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employee.entity.EmployeeEntity;
import com.employee.entity.ResetTokenEntity;

public interface ResetTokenRepository extends JpaRepository<ResetTokenEntity, String> {
	
	ResetTokenEntity findByToken(String token);
	
	ResetTokenEntity findByUser(EmployeeEntity newUser);

}
