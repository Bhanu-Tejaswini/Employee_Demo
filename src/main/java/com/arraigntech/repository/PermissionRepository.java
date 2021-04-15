package com.arraigntech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arraigntech.entity.Permission;

@Repository	
public interface PermissionRepository extends JpaRepository<Permission,String> {

	Permission findByName(String name);
}
