package com.arraigntech.service.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.arraigntech.entity.Permission;
import com.arraigntech.exceptions.AppException;
import com.arraigntech.repository.PermissionRepository;
import com.arraigntech.service.IVSService;
import com.arraigntech.utility.MessageConstants;

@Service
public class PermissionServiceImpl implements IVSService<Permission, String> {

	@Autowired
	private PermissionRepository permissionRepo;

	@Override
	public Page<Permission> getPaginated(Integer page, Integer limit) {
		return permissionRepo.findAll(PageRequest.of(page, limit));
	}

	@Override
	public List<Permission> getAll() throws AppException {
		List<Permission> permissions = permissionRepo.findAll();
		if (permissions.isEmpty()) {
			throw new AppException(MessageConstants.DATA_NOT_FOUND);
		}
		return permissions;
	}

	@Override
	public Permission create(Permission permission) {
		Permission newPermission = new Permission();
		newPermission.setName(permission.getName());
		return permissionRepo.save(newPermission);
	}

	@Override
	public Permission update(Permission entity) {
		return permissionRepo.save(entity);
	}


	public boolean delete(String id) throws AppException {
		Optional<Permission> permission = permissionRepo.findById(id);
		if (!permission.isPresent()) {
			throw new AppException(MessageConstants.DATA_NOT_FOUND);
		}
		permissionRepo.deleteById(id);
		return true;
	}

	@Override
	public Permission getById(String id) throws AppException {
		Optional<Permission> permission = permissionRepo.findById(id);
		if (!permission.isPresent()) {
			throw new AppException(MessageConstants.DATA_NOT_FOUND);
		}
		return permission.get();
	}

}
