package com.example.demo.controller;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import com.example.demo.model.response.BaseResponse;
import com.example.demo.service.impl.MailServiceImpl;
import com.example.demo.service.impl.UserServiceImpl;
import com.example.demo.utility.IVSJwtUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class AuthController {
	
	public static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private IVSJwtUtil jwtUtil;

	@Autowired
	private MailServiceImpl mailService;

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

	
	@ApiOperation(value = "save and register user")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/register", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> saveAndUpdateCustomer(@RequestBody UserDTO user)  {
		log.debug("save and register user");
		return new BaseResponse<Boolean>(userService.register(user)).withSuccess(true);
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
