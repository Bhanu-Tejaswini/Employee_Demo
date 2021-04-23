package com.arraigntech.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.arraigntech.Exception.AppException;
import com.arraigntech.entity.Permission;
import com.arraigntech.entity.Role;
import com.arraigntech.entity.User;
import com.arraigntech.model.RoleDTO;
import com.arraigntech.service.impl.PermissionServiceImpl;
import com.arraigntech.service.impl.RoleServiceImpl;
import com.arraigntech.service.impl.UserServiceImpl;

@RestController
@CrossOrigin(origins="*")
public class AdminController {
	
	public static final Logger log = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private RoleServiceImpl roleService;

	@Autowired
	private PermissionServiceImpl permissionService;

	@Autowired
	private UserServiceImpl userService;

	@PostMapping("/role")
	public ResponseEntity<Void> registerRole(@RequestBody RoleDTO role) {
		try {
			roleService.createRole(role);
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/role")
	public Role updateRole(@RequestBody Role role) {
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
		List<Role> roles = new ArrayList<>();
		try {
			roles = roleService.getAll();
			return ResponseEntity.ok(roles);
		} catch (AppException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/role-page")
	public Page<Role> getPaginatedRole(@RequestParam("page") int page, @RequestParam("limit") int limit) {
		return roleService.getPaginated(page, limit);
	}

	@PostMapping("/permission")
	public ResponseEntity<Void> registerPermission(@RequestBody Permission permission) {
		try {
			permissionService.create(permission);
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/permission")
	public Permission updatePermission(@RequestBody Permission permission) {
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
		List<Permission> permissions = new ArrayList<>();
		try {
			permissions = permissionService.getAll();
			return ResponseEntity.ok(permissions);
		} catch (AppException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/permission-page")
	public Page<Permission> getPaginatedPermission(@RequestParam("page") int page, @RequestParam("limit") int limit) {
		return permissionService.getPaginated(page, limit);
	}

	@GetMapping("/users")
	public ResponseEntity<?> getAllUsers() {
		List<User> users = new ArrayList<>();
		try {
			users = userService.getAll();
			return ResponseEntity.ok(users);
		} catch (AppException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
