package com.employee.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee.entity.PermissionEntity;
import com.employee.entity.RoleEntity;
import com.employee.exceptions.AppException;
import com.employee.repository.PermissionRepository;
import com.employee.repository.RoleRepository;
import com.employee.request.vo.RoleVO;
import com.employee.utility.MessageConstants;


@Service
public class RoleServiceImpl  {

	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PermissionRepository permissionRepo;


	/**
	 * Creates the role.
	 *
	 * @param role the role
	 * @return the role
	 */
	public RoleEntity createRole(RoleVO role) {
		RoleEntity newRole=new RoleEntity();
		newRole.setName(role.getName());
		for(String str:role.getPermission()) {
			PermissionEntity permission=permissionRepo.findByName(str);
			newRole.getPermissions().add(permission);
		}
		return roleRepo.save(newRole);
	}

	public RoleEntity update(RoleEntity entity) {
		for (PermissionEntity permission : entity.getPermissions()) {
			entity.getPermissions().add(permission);
		}
		return roleRepo.save(entity);
	}


	public boolean delete(String id) throws AppException {
		Optional<RoleEntity> newRole = roleRepo.findById(id);
		if (!newRole.isPresent()) {
			throw new AppException(MessageConstants.DATA_NOT_FOUND);
		}
		roleRepo.deleteById(id);
		return true;
	}

	public List<RoleEntity> getAll() throws AppException {
		List<RoleEntity> roles = (List<RoleEntity>) roleRepo.findAll();
		if (roles.isEmpty()) {
			throw new AppException(MessageConstants.DATA_NOT_FOUND);
		}
		return roles;
	}

	public RoleEntity getById(String id) throws AppException {
		Optional<RoleEntity> role = roleRepo.findById(id);
		if (!role.isPresent()) {
			throw new AppException(MessageConstants.DATA_NOT_FOUND);
		}
		return role.get();
	}


}
