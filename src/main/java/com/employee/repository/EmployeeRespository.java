package com.employee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.employee.entity.EmployeeEntity;

@Repository
public interface EmployeeRespository extends JpaRepository<EmployeeEntity, String> {

	@Query(value = "SELECT * FROM Employee u WHERE u.username = ?1 and u.active=1", nativeQuery = true)
	Optional<EmployeeEntity> findByUsername(String name);
	
	@Query(value = "SELECT * FROM Employee u WHERE u.username = ?1", nativeQuery = true)
	Optional<EmployeeEntity> findByUsernameAll(String name);

	@Query(value = "SELECT * FROM Employee u WHERE u.email = ?1 and u.active=1", nativeQuery = true)
	EmployeeEntity findByEmail(String email);
	
	@Query(value = "SELECT * FROM Employee u WHERE u.email = ?1", nativeQuery = true)
	EmployeeEntity findByEmailAll(String email);
	
//	@Query(value="select a from app_user a where a.email= ?1 and a.id <> ?2",nativeQuery=true)
	EmployeeEntity findByEmailAndIdNot(String email,String id);
	
	EmployeeEntity findByUsernameAndIdNot(String username, String id);

}
