package com.example.demo.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.Exception.DataExistsException;
import com.example.demo.Exception.DataNotFoundException;
import com.example.demo.Exception.UserTokenNotFoundException;
import com.example.demo.model.IVSPassword;
import com.example.demo.model.IVSTokenEmail;
import com.example.demo.model.LoginDetails;
import com.example.demo.model.UserDTO;
import com.example.demo.service.MailService;
import com.example.demo.service.UserService;
import com.example.demo.utility.IVSJwtUtil;

@RestController
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private IVSJwtUtil jwtUtil;

	@Autowired
	private MailService mailService;

	@GetMapping("/user")
	@PreAuthorize("hasAuthority('create_profile')")
	public String getUser() {
		return "This is user data";
	}

	@GetMapping("/admin")
//	@PreAuthorize("hasAuthority('ROLE_user')")
	
	public String getAdmin() {
		return "This is admin data";
	}

	@PostMapping("/register")
	public ResponseEntity<Void> saveUser(@RequestBody UserDTO user) {
		try {
			userService.register(user);
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (DataExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody IVSTokenEmail tokenEmail, UriComponentsBuilder builder) {

		String token = jwtUtil.generateResetToken(tokenEmail.getEmail());
		// Generating the password reset link
		String passwordResetLink = builder.path("/forgot-password").queryParam("token", token).buildAndExpand(token)
				.toUriString();
		// Sending the mail with password reset link
		try {
			mailService.sendEmail(tokenEmail.getEmail(), passwordResetLink);
		} catch (UnsupportedEncodingException | MessagingException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return ResponseEntity.ok(passwordResetLink);
	}
	
	@PostMapping("/login")
	public String login(@RequestBody LoginDetails login, UriComponentsBuilder builder) {
		return userService.generateToken(login,builder);
	}

	@PostMapping("/reset-password/{token}")
	public ResponseEntity<?> resetPassword(@PathVariable("token") String token, @RequestBody IVSPassword pass) {
		try {
			userService.updatePassword(token, pass.getPassword());
		} catch (DataNotFoundException | UserTokenNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return ResponseEntity.ok("password changed");
	}

}
