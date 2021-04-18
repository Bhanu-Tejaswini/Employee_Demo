package com.arraigntech.controller;

import java.net.HttpURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.arraigntech.entity.response.BaseResponse;
import com.arraigntech.model.IVSPassword;
import com.arraigntech.model.IVSTokenEmail;
import com.arraigntech.model.LoginDetails;
import com.arraigntech.model.UserDTO;
import com.arraigntech.service.impl.UserServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

	public static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserServiceImpl userService;

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
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<Boolean> saveAndUpdateCustomer(@RequestBody UserDTO user) {
		log.debug("save and register user");
		return new BaseResponse<Boolean>(userService.register(user)).withSuccess(true);
	}

	@ApiOperation(value = "save and register user")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/forgot-password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<String> forgotPassword(@RequestBody IVSTokenEmail tokenEmail, UriComponentsBuilder builder) {
		return new BaseResponse<String>(userService.forgotPassword(tokenEmail.getEmail(), builder)).withSuccess(true);
	}

	@ApiOperation(value = "User Login")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<String> login(@RequestBody LoginDetails login, UriComponentsBuilder builder) {
		return new BaseResponse<String>(userService.generateToken(login, builder)).withSuccess(true);
	}

	@ApiOperation(value = "Reseting the password")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/reset-password/{token}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<String> resetPassword(@PathVariable("token") String token, @RequestBody IVSPassword pass) {
		return new BaseResponse<String>(userService.updatePassword(token, pass.getPassword())).withSuccess(true);
	}

}
