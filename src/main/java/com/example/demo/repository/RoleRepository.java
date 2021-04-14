package com.example.demo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Role;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, String> {

	Role findByName(String name);

}
