package com.arraigntech.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.arraigntech.entity.Permission;
import com.arraigntech.entity.Role;
import com.arraigntech.exceptions.AppException;
import com.arraigntech.model.RoleDTO;
import com.arraigntech.repository.PermissionRepository;
import com.arraigntech.repository.RoleRepository;
import com.arraigntech.service.IVSService;
import com.arraigntech.utility.MessageConstants;

/**
 * @author Bhaskara S
 *
 */
@Service
public class RoleServiceImpl implements IVSService<Role, String> {

	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PermissionRepository permissionRepo;

	@Override
	public Page<Role> getPaginated(Integer page, Integer limit) {
		return roleRepo.findAll(PageRequest.of(page, limit));
	}


	/**
	 * Creates the role.
	 *
	 * @param role the role
	 * @return the role
	 */
	public Role createRole(RoleDTO role) {
		Role newRole=new Role();
		newRole.setName(role.getName());
		for(String str:role.getPermission()) {
			Permission permission=permissionRepo.findByName(str);
			newRole.getPermissions().add(permission);
		}
		return roleRepo.save(newRole);
	}

	@Override
	public Role update(Role entity) {
		for (Permission permission : entity.getPermissions()) {
			entity.getPermissions().add(permission);
		}
		return roleRepo.save(entity);
	}


	public boolean delete(String id) throws AppException {
		Optional<Role> newRole = roleRepo.findById(id);
		if (!newRole.isPresent()) {
			throw new AppException(MessageConstants.DATA_NOT_FOUND);
		}
		roleRepo.deleteById(id);
		return true;
	}

	@Override
	public List<Role> getAll() throws AppException {
		List<Role> roles = (List<Role>) roleRepo.findAll();
		if (roles.isEmpty()) {
			throw new AppException(MessageConstants.DATA_NOT_FOUND);
		}
		return roles;
	}

	@Override
	public Role getById(String id) throws AppException {
		Optional<Role> role = roleRepo.findById(id);
		if (!role.isPresent()) {
			throw new AppException(MessageConstants.DATA_NOT_FOUND);
		}
		return role.get();
	}


	@Override
	public Role create(Role entity) {
		// TODO Auto-generated method stub
		return null;
	}
}
