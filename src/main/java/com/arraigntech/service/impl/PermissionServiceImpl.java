package com.arraigntech.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.arraigntech.Exception.DataNotFoundException;
import com.arraigntech.entity.Permission;
import com.arraigntech.repository.PermissionRepository;
import com.arraigntech.service.IVSService;

@Service
public class PermissionServiceImpl implements IVSService<Permission, String> {

	@Autowired
	private PermissionRepository permissionRepo;

	@Override
	public Page<Permission> getPaginated(Integer page, Integer limit) {
		return permissionRepo.findAll(PageRequest.of(page, limit));
	}

	@Override
	public List<Permission> getAll() throws DataNotFoundException {
		List<Permission> permissions = permissionRepo.findAll();
		if (permissions.isEmpty()) {
			throw new DataNotFoundException("There are no permissions to display");
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

	@Override
	public boolean delete(String id) throws DataNotFoundException {
		Optional<Permission> permission = permissionRepo.findById(id);
		if (!permission.isPresent()) {
			throw new DataNotFoundException("The specified permission is not found");
		}
		permissionRepo.deleteById(id);
		return true;
	}

	@Override
	public Permission getById(String id) throws DataNotFoundException {
		Optional<Permission> permission = permissionRepo.findById(id);
		if (!permission.isPresent()) {
			throw new DataNotFoundException("The required permission does not found");
		}
		return permission.get();
	}

}
