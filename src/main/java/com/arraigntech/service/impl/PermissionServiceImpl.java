package com.arraigntech.service.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.arraigntech.entity.PermissionEntity;
import com.arraigntech.exceptions.AppException;
import com.arraigntech.repository.PermissionRepository;
import com.arraigntech.service.IVSService;
import com.arraigntech.utility.MessageConstants;

@Service
public class PermissionServiceImpl implements IVSService<PermissionEntity, String> {

	@Autowired
	private PermissionRepository permissionRepo;

	@Override
	public Page<PermissionEntity> getPaginated(Integer page, Integer limit) {
		return permissionRepo.findAll(PageRequest.of(page, limit));
	}

	@Override
	public List<PermissionEntity> getAll() throws AppException {
		List<PermissionEntity> permissions = permissionRepo.findAll();
		if (permissions.isEmpty()) {
			throw new AppException(MessageConstants.DATA_NOT_FOUND);
		}
		return permissions;
	}

	@Override
	public PermissionEntity create(PermissionEntity permission) {
		PermissionEntity newPermission = new PermissionEntity();
		newPermission.setName(permission.getName());
		return permissionRepo.save(newPermission);
	}

	@Override
	public PermissionEntity update(PermissionEntity entity) {
		return permissionRepo.save(entity);
	}


	public boolean delete(String id) throws AppException {
		Optional<PermissionEntity> permission = permissionRepo.findById(id);
		if (!permission.isPresent()) {
			throw new AppException(MessageConstants.DATA_NOT_FOUND);
		}
		permissionRepo.deleteById(id);
		return true;
	}

	@Override
	public PermissionEntity getById(String id) throws AppException {
		Optional<PermissionEntity> permission = permissionRepo.findById(id);
		if (!permission.isPresent()) {
			throw new AppException(MessageConstants.DATA_NOT_FOUND);
		}
		return permission.get();
	}

}
