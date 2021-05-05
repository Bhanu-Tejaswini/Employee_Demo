package com.arraigntech.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.arraigntech.entity.User;

@Repository
public interface UserRespository extends JpaRepository<User, String> {

	@Query(value = "SELECT * FROM app_user u WHERE u.username = ?1 and u.active=1", nativeQuery = true)
	Optional<User> findByUsername(String name);
	
	@Query(value = "SELECT * FROM app_user u WHERE u.username = ?1", nativeQuery = true)
	Optional<User> findByUsernameAll(String name);

	@Query(value = "SELECT * FROM app_user u WHERE u.email = ?1 and u.active=1", nativeQuery = true)
	User findByEmail(String email);
	
	@Query(value = "SELECT * FROM app_user u WHERE u.email = ?1", nativeQuery = true)
	User findByEmailAll(String email);
	
//	@Query(value="select a from app_user a where a.email= ?1 and a.id <> ?2",nativeQuery=true)
	User findByEmailAndIdNot(String email,String id);
	
	User findByUsernameAndIdNot(String username, String id);

}
