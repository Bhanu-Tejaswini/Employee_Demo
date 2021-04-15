package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.Exception.DataNotFoundException;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.model.RoleDTO;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.IVSService;

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

	@Override
	public boolean delete(String id) throws DataNotFoundException {
		Optional<Role> newRole = roleRepo.findById(id);
		if (!newRole.isPresent()) {
			throw new DataNotFoundException("The specified role not found");
		}
		roleRepo.deleteById(id);
		return true;
	}

	@Override
	public List<Role> getAll() throws DataNotFoundException {
		List<Role> roles = (List<Role>) roleRepo.findAll();
		if (roles.isEmpty()) {
			throw new DataNotFoundException("Their are no roles to display");
		}
		return roles;
	}

	@Override
	public Role getById(String id) throws DataNotFoundException {
		Optional<Role> role = roleRepo.findById(id);
		if (!role.isPresent()) {
			throw new DataNotFoundException("The role does not exists");
		}
		return role.get();
	}


	@Override
	public Role create(Role entity) {
		// TODO Auto-generated method stub
		return null;
	}
}
