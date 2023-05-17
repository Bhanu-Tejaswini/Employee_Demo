package com.employee.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.employee.entity.RoleEntity;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<RoleEntity, String> {

	RoleEntity findByName(String name);

}
