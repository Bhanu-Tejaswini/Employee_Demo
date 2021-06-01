package com.arraigntech.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.arraigntech.entity.RoleEntity;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<RoleEntity, String> {

	RoleEntity findByName(String name);

}
