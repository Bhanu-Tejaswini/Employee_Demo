package com.arraigntech.controller;

import java.net.HttpURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.arraigntech.entity.response.BaseResponse;
import com.arraigntech.model.IVSResetPassword;
import com.arraigntech.model.IVSTokenEmail;
import com.arraigntech.model.LoginDetails;
import com.arraigntech.model.UserDTO;
import com.arraigntech.service.impl.UserServiceImpl;
import com.arraigntech.utility.MessageConstants;

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
	@PreAuthorize("hasAuthority('update_profile')")
	public String getUser() {
		return "This is user data";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasAuthority('create_profile')")
	public String getAdmin() {
		return "This is admin data";
	}

	@ApiOperation(value = "save and register user")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> saveAndUpdateCustomer(@RequestBody UserDTO user) {
		log.debug("save and register user");
		return new BaseResponse<Boolean>(userService.register(user)).withSuccess(true);
	}

	@ApiOperation(value = "Forgot password")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/forgot-password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> forgotPassword(@RequestBody IVSTokenEmail tokenEmail) {
		log.debug("Forgot password");
		return new BaseResponse<Boolean>(userService.forgotPassword(tokenEmail.getEmail())).withSuccess(true);
	}

	@ApiOperation(value = "User Login")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> login(@RequestBody LoginDetails login, UriComponentsBuilder builder) {
		log.debug("User Login");
		return new BaseResponse<String>(userService.generateToken(login, builder)).withSuccess(true);
	}

	@ApiOperation(value = "Reseting the password")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/reset-password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> resetPassword(@RequestHeader(name="Authorization", required=true) String token, @RequestBody IVSResetPassword pass) {
		log.debug("Reseting the password");
//		return new BaseResponse<String>(userService.updatePassword(token, pass.getPassword())).withSuccess(true);

		userService.updatePassword(token, pass.getPassword());
		BaseResponse<String> response = new BaseResponse<>();
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.PASSWORDMESSAGE).build();
	}
	
	@PostMapping("/test")
	public String getToken(@RequestBody UserDTO user) {
		return userService.getToken(user);
	}
}
