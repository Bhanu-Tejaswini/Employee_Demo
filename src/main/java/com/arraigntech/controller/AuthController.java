package com.arraigntech.controller;

import java.net.HttpURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arraigntech.model.IVSResetPassword;
import com.arraigntech.model.IVSTokenEmail;
import com.arraigntech.model.LoginDetails;
import com.arraigntech.model.LoginResponseDTO;
import com.arraigntech.model.SocialLoginDTO;
import com.arraigntech.model.UserDTO;
import com.arraigntech.model.response.BaseResponse;
import com.arraigntech.service.impl.SocialLoginServiceImpl;
import com.arraigntech.service.impl.UserServiceImpl;
import com.arraigntech.utility.MessageConstants;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/auth")
public class AuthController {

	public static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private SocialLoginServiceImpl socialLoginService;

	@ApiOperation(value = "save and register user")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> saveAndUpdateCustomer(@RequestBody UserDTO user) {
		if(log.isDebugEnabled()) {
			log.debug("save and register user {}.",user);
		}	
		return new BaseResponse<Boolean>(userService.register(user)).withSuccess(true);
	}

	@ApiOperation(value = "Forgot password")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/forgot-password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> forgotPassword(@RequestBody IVSTokenEmail tokenEmail) {
		if(log.isDebugEnabled()) {
			log.debug("Forgot password {}.",tokenEmail);
		}
		return new BaseResponse<Boolean>(userService.forgotPassword(tokenEmail.getEmail())).withSuccess(true);
	}

	@ApiOperation(value = "User Login")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> login(@RequestBody LoginDetails login) {
		if(log.isDebugEnabled()) {
			log.debug("User Login {}.",login);
		}
		BaseResponse<String> response = new BaseResponse<>();
		LoginResponseDTO responseMessage = userService.generateToken(login);
		return responseMessage.isFlag()
				? response.withSuccess(true)
						.withResponseMessage("Token", responseMessage.getResult()).build()
				: response.withSuccess(false)
						.withResponseMessage("Message", responseMessage.getResult())
						.build();

	}

	@ApiOperation(value = "Reseting the password")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/reset-password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> resetPassword(@RequestHeader(name = "Authorization", required = true) String token,
			@RequestBody IVSResetPassword pass) {
		if(log.isDebugEnabled()) {
			log.debug("Reseting the password {}",token);
		}
		BaseResponse<String> response = new BaseResponse<>();
		userService.updatePassword(token, pass.getPassword());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.PASSWORDMESSAGE).build();
	}


	@ApiOperation(value = "Google signin")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/google-login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> getGoogleToken(@RequestBody SocialLoginDTO socialLogin) {
		if(log.isDebugEnabled()) {
			log.debug("Google signin {}.",socialLogin);
		}
		BaseResponse<String> response = new BaseResponse<>();
		LoginResponseDTO responseMessage = socialLoginService.getGoogleToken(socialLogin);
		return responseMessage.isFlag()
				? response.withSuccess(true)
						.withResponseMessage("Token", responseMessage.getResult()).build()
				: response.withSuccess(false)
						.withResponseMessage("Message", responseMessage.getResult())
						.build();
	}
	
	@ApiOperation(value = "Facebook signin")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/facebook-login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> getFacebookToken(@RequestBody SocialLoginDTO socialLogin) {
		if(log.isDebugEnabled()) {
			log.debug("Facebook signin {}.",socialLogin);
		}
		BaseResponse<String> response = new BaseResponse<>();
		LoginResponseDTO responseMessage = socialLoginService.getFacebookToken(socialLogin);
		return responseMessage.isFlag()
				? response.withSuccess(true)
						.withResponseMessage("Token", responseMessage.getResult()).build()
				: response.withSuccess(false)
						.withResponseMessage("Message", responseMessage.getResult())
						.build();
	}

	
	@ApiOperation(value = "registration link verification")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/verify/token", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> registerationLink(@RequestHeader(name = "Authorization", required = true) String token) {
		if(log.isDebugEnabled()) {
			log.debug("registration link verification {}",token);
		}
		Boolean result = userService.verifyRegisterationToken(token);
		BaseResponse<Boolean> response = new BaseResponse<>();
		return result
				? response.withSuccess(true)
						.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.EMAIL_SUCCESS).build()
				: response.withSuccess(false)
						.withResponseMessage(MessageConstants.KEY_FAIL, MessageConstants.VERIFICATION_EMAIL_FAIL)
						.build();

	}
	
	@ApiOperation(value = "Resend registration link")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/resend-verifylink", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> resendRegisterationLink(@RequestBody IVSTokenEmail emailDTO) {
		if(log.isDebugEnabled()) {
			log.debug("Resend registration link {}.",emailDTO);
		}
		Boolean result = userService.sendRegisterationLink(emailDTO.getEmail());
		BaseResponse<Boolean> response = new BaseResponse<>();
		return result
				? response.withSuccess(true)
						.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.EMAIL_SUCCESS).build()
				: response.withSuccess(false)
						.withResponseMessage(MessageConstants.KEY_FAIL, MessageConstants.VERIFICATION_EMAIL_FAIL)
						.build();

	}
}
