package com.arraigntech.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.arraigntech.entity.UserEntity;

@Repository
public interface UserRespository extends JpaRepository<UserEntity, String> {

	@Query(value = "SELECT * FROM app_user u WHERE u.username = ?1 and u.active=1", nativeQuery = true)
	Optional<UserEntity> findByUsername(String name);
	
	@Query(value = "SELECT * FROM app_user u WHERE u.username = ?1", nativeQuery = true)
	Optional<UserEntity> findByUsernameAll(String name);

	@Query(value = "SELECT * FROM app_user u WHERE u.email = ?1 and u.active=1", nativeQuery = true)
	UserEntity findByEmail(String email);
	
	@Query(value = "SELECT * FROM app_user u WHERE u.email = ?1", nativeQuery = true)
	UserEntity findByEmailAll(String email);
	
//	@Query(value="select a from app_user a where a.email= ?1 and a.id <> ?2",nativeQuery=true)
	UserEntity findByEmailAndIdNot(String email,String id);
	
	UserEntity findByUsernameAndIdNot(String username, String id);

}
