package com.arraigntech.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.arraigntech.entity.User;

@Repository
public interface UserRespository extends JpaRepository<User, String> {

	Optional<User> findByUsername(String name);

	@Query(value = "SELECT * FROM app_user u WHERE u.email = ?1", nativeQuery = true)
	User findByEmail(String email);

}
