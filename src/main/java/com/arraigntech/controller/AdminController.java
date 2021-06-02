package com.arraigntech.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.arraigntech.entity.PermissionEntity;
import com.arraigntech.entity.RoleEntity;
import com.arraigntech.exceptions.AppException;
import com.arraigntech.request.RoleVO;
import com.arraigntech.service.impl.PermissionServiceImpl;
import com.arraigntech.service.impl.RoleServiceImpl;

@RestController
public class AdminController {
	

	@Autowired
	private RoleServiceImpl roleService;

	@Autowired
	private PermissionServiceImpl permissionService;


	@PostMapping("/role")
	public ResponseEntity<Void> registerRole(@RequestBody RoleVO role) {
		try {
			roleService.createRole(role);
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/role")
	public RoleEntity updateRole(@RequestBody RoleEntity role) {
		return roleService.update(role);
	}

	@DeleteMapping("/role/{id}")
	public ResponseEntity<Void> deleteRole(@PathVariable("id") String id) {
		try {
			roleService.delete(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (AppException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/role")
//	@PreAuthorize("hasAuthority('ROLE_admin')")
	public ResponseEntity<?> getAllRole() {
		List<RoleEntity> roles = new ArrayList<>();
		try {
			roles = roleService.getAll();
			return ResponseEntity.ok(roles);
		} catch (AppException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/role-page")
	public Page<RoleEntity> getPaginatedRole(@RequestParam("page") int page, @RequestParam("limit") int limit) {
		return roleService.getPaginated(page, limit);
	}

	@PostMapping("/permission")
	public ResponseEntity<Void> registerPermission(@RequestBody PermissionEntity permission) {
		try {
			permissionService.create(permission);
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/permission")
	public PermissionEntity updatePermission(@RequestBody PermissionEntity permission) {
		return permissionService.update(permission);
	}

	@DeleteMapping("/permission/{id}")
	public ResponseEntity<Void> deletePermission(@PathVariable("id") String id) {
		try {
			permissionService.delete(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (AppException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/permission")
	public ResponseEntity<?> getAllPermission() {
		List<PermissionEntity> permissions = new ArrayList<>();
		try {
			permissions = permissionService.getAll();
			return ResponseEntity.ok(permissions);
		} catch (AppException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/permission-page")
	public Page<PermissionEntity> getPaginatedPermission(@RequestParam("page") int page, @RequestParam("limit") int limit) {
		return permissionService.getPaginated(page, limit);
	}

}
