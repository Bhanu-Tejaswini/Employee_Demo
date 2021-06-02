package com.arraigntech.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.arraigntech.entity.PermissionEntity;
import com.arraigntech.entity.RoleEntity;
import com.arraigntech.exceptions.AppException;
import com.arraigntech.repository.PermissionRepository;
import com.arraigntech.repository.RoleRepository;
import com.arraigntech.request.RoleVO;
import com.arraigntech.service.IVSService;
import com.arraigntech.utility.MessageConstants;

/**
 * @author Bhaskara S
 *
 */
@Service
public class RoleServiceImpl implements IVSService<RoleEntity, String> {

	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PermissionRepository permissionRepo;

	@Override
	public Page<RoleEntity> getPaginated(Integer page, Integer limit) {
		return roleRepo.findAll(PageRequest.of(page, limit));
	}


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

	@Override
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

	@Override
	public List<RoleEntity> getAll() throws AppException {
		List<RoleEntity> roles = (List<RoleEntity>) roleRepo.findAll();
		if (roles.isEmpty()) {
			throw new AppException(MessageConstants.DATA_NOT_FOUND);
		}
		return roles;
	}

	@Override
	public RoleEntity getById(String id) throws AppException {
		Optional<RoleEntity> role = roleRepo.findById(id);
		if (!role.isPresent()) {
			throw new AppException(MessageConstants.DATA_NOT_FOUND);
		}
		return role.get();
	}


	@Override
	public RoleEntity create(RoleEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}
}
