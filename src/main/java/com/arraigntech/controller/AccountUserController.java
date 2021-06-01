package com.arraigntech.controller;

import java.net.HttpURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arraigntech.model.EmailSettingsModel;
import com.arraigntech.model.IVSPassword;
import com.arraigntech.model.IVSResetPassword;
import com.arraigntech.model.OverLayImageVO;
import com.arraigntech.model.UserSettingsDTO;
import com.arraigntech.model.response.BaseResponse;
import com.arraigntech.service.AccountSettingService;
import com.arraigntech.service.impl.UserServiceImpl;
import com.arraigntech.utility.MessageConstants;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/account")
public class AccountUserController {
	
	public static final Logger log = LoggerFactory.getLogger(AccountUserController.class);
	public static final String MOBILENUMBER_VERIFIED="MobileNumberVerified";
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	protected AccountSettingService accountSettingService;
	
	@ApiOperation(value = "Update password")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/update-password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> resetPassword(@RequestBody IVSPassword pass) {
		if(log.isDebugEnabled()) {
			log.debug("Update password {}.",pass);
		}
		return new BaseResponse<String>(userService.updateAccountPassword(pass.getPassword(),pass.getNewpassword())).withSuccess(true);
	}

	@ApiOperation(value = "Fetch email Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/email-settings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<EmailSettingsModel> getEmailSettings() {
		if(log.isDebugEnabled()) {
			log.debug("Fetch email Settings");
		}
		return new BaseResponse<EmailSettingsModel>(userService.getEmailSetting()).withSuccess(true);
	}
	
	@ApiOperation(value = "save email Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/email-settings", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> postEmailSettings(@RequestBody EmailSettingsModel emailSettingsModel) {
		if(log.isDebugEnabled()) {
			log.debug("save email Settings {}.",emailSettingsModel);
		}
		return new BaseResponse<String>(userService.saveEmailSettings(emailSettingsModel)).withSuccess(true);
	}
	
	@ApiOperation(value = "Fetch user Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/user-settings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<UserSettingsDTO> getUserSettings() {
		if(log.isDebugEnabled()) {
			log.debug("Fetch user Settings");
		}
		return new BaseResponse<UserSettingsDTO>(accountSettingService.fetchUserSettings()).withSuccess(true);
	}
	
	@ApiOperation(value = "send a OTP for mobile number verification")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/sendOTP", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> sendOTPForUser(@RequestBody UserSettingsDTO userSettings) {
		if(log.isDebugEnabled()) {
			log.debug("send a OTP for user {}.",userSettings);
		}
		return new BaseResponse<Boolean>(accountSettingService.sendOTPForUser(userSettings))
				.withSuccess(true);
	}
	
	@ApiOperation(value = "save userName Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "username/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> userNameUpdate(@RequestBody UserSettingsDTO userSettings) {
		if(log.isDebugEnabled()) {
			log.debug("save userName Settings {}.",userSettings);
		}
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.saveUserName(userSettings.getUsername());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.USERNAME_UPDATE).build();
	}
	
	@ApiOperation(value = "save email Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "email/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> emailUpdate(@RequestBody UserSettingsDTO userSettings) {
		if(log.isDebugEnabled()) {
			log.debug("save email Settings {}.",userSettings);
		}
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.updateEmail(userSettings.getEmail());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.EMAILSETTINGSMESSAGE).build();
	}
	
	@ApiOperation(value = "save pincode Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "pincode/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> pincodeUpdate(@RequestBody UserSettingsDTO userSettings) {
		if(log.isDebugEnabled()) {
			log.debug("save pincode Settings {}.",userSettings);
		}
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.updatePincode(userSettings.getPincode());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.PINCODE_UPDATE).build();
	}
	
	@ApiOperation(value = "save language Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "language/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> languageUpdate(@RequestBody UserSettingsDTO userSettings) {
		if(log.isDebugEnabled()) {
			log.debug("save language Settings {}.",userSettings);
		}
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.updateLanguage(userSettings.getLanguage());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.LANGUAGE_UPDATE).build();
	}
	
	@ApiOperation(value = "save timeZone Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "timeZone/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> timeZoneUpdate(@RequestBody UserSettingsDTO userSettings) {
		if(log.isDebugEnabled()) {
			log.debug("save timeZone Settings {}.",userSettings);
		}
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.updateTimeZone(userSettings.getTimezone());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.TIMEZONE_UPDATE).build();
	}
	
	@ApiOperation(value = "verify OTP")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "verify/otp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> otpVeriifcation(@RequestBody UserSettingsDTO userSettings) {
		if(log.isDebugEnabled()) {
			log.debug("verify OTP {}.",userSettings);
		}
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.verifyCode(userSettings);
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.OTP_VERIFICATION).build();
	}
	
	@ApiOperation(value = "Delete user account")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/user", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> deleteUser(@RequestBody IVSResetPassword password) {
		if(log.isDebugEnabled()) {
			log.debug("Delete user account {}.", password);
		}
		BaseResponse<Boolean> response = new BaseResponse<>();
		userService.delete(password.getPassword());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.USER_DELETED).build();
	}
	
	@ApiOperation(value = "verify mobile number")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "verify/mobilenumber", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> mobilenumberVerification() {
		if(log.isDebugEnabled()) {
			log.debug("verify mobile number");
		}
		BaseResponse<Boolean> response = new BaseResponse<>();
		Boolean result = accountSettingService.verifyMobileNumber();
		return result?
				response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, "true")
			:	response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, "false");
	}
}
