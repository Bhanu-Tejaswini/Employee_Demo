package com.arraigntech.controller;

import java.net.HttpURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arraigntech.entity.response.BaseResponse;
import com.arraigntech.model.AccountSettingVO;
import com.arraigntech.model.EmailSettingsModel;
import com.arraigntech.model.IVSPassword;
import com.arraigntech.model.UserSettingsDTO;
import com.arraigntech.service.AccountSettingService;
import com.arraigntech.service.impl.UserServiceImpl;
import com.arraigntech.utility.MessageConstants;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/account")
public class AccountUserController {
	
	public static final Logger log = LoggerFactory.getLogger(AccountUserController.class);
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	protected AccountSettingService accountSettingService;
	
	@ApiOperation(value = "Update password")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/update-password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<String> resetPassword(@RequestBody IVSPassword pass) {
		return new BaseResponse<String>(userService.updateAccountPassword(pass.getPassword(),pass.getNewpassword())).withSuccess(true);
	}

	@ApiOperation(value = "Fetch email Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/email-settings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<EmailSettingsModel> getEmailSettings() {
		return new BaseResponse<EmailSettingsModel>(userService.getEmailSetting()).withSuccess(true);
	}
	
	@ApiOperation(value = "save email Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/email-settings", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<String> postEmailSettings(@RequestBody EmailSettingsModel emailSettingsModel) {
		return new BaseResponse<String>(userService.saveEmailSettings(emailSettingsModel)).withSuccess(true);
	}
	
	@ApiOperation(value = "Fetch user Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/user-settings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<UserSettingsDTO> getUserSettings() {
		return new BaseResponse<UserSettingsDTO>(accountSettingService.fetchUserSettings()).withSuccess(true);
	}
	
//	@ApiOperation(value = "save user Settings")
//	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
//	@RequestMapping(value = "/user-settings", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	@CrossOrigin(origins = "*")
//	public BaseResponse<String> postUserSettings(@RequestBody UserSettingsDTO userSettings) {
//		return new BaseResponse<String>(userService.saveUserSettings(userSettings)).withSuccess(true);
//	}
	
	
	@ApiOperation(value = "send a OTP for mobile number verification")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/sendOTP", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<Boolean> sendOTPForUser(@RequestBody UserSettingsDTO userSettings) {
		log.debug("send a OTP for user");
		return new BaseResponse<Boolean>(accountSettingService.sendOTPForUser(userSettings.getMobilenumber()))
				.withSuccess(true);
	}
	
	@ApiOperation(value = "save userName Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "username/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<Boolean> userNameUpdate(@RequestBody UserSettingsDTO userSettings) {
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.saveUserName(userSettings.getUsername());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.USERNAME_UPDATE).build();
	}
	
	@ApiOperation(value = "save email Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "email/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<Boolean> emailUpdate(@RequestBody UserSettingsDTO userSettings) {
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.updateEmail(userSettings.getEmail());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.EMAILSETTINGSMESSAGE).build();
	}
	
	@ApiOperation(value = "save mobilenumber Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "mobilenumber/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<Boolean> mobileNumberUpdate(@RequestBody UserSettingsDTO userSettings) {
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.updateMobileNumber(userSettings.getMobilenumber());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.MOBILENUMBER_UPDATE).build();
	}
	
	@ApiOperation(value = "save pincode Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "pincode/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<Boolean> pincodeUpdate(@RequestBody UserSettingsDTO userSettings) {
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.updatePincode(userSettings.getPincode());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.PINCODE_UPDATE).build();
	}
	
	@ApiOperation(value = "save language Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "language/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<Boolean> languageUpdate(@RequestBody UserSettingsDTO userSettings) {
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.updateLanguage(userSettings.getLanguage());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.LANGUAGE_UPDATE).build();
	}
	
	@ApiOperation(value = "save timeZone Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "timeZone/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<Boolean> timeZoneUpdate(@RequestBody UserSettingsDTO userSettings) {
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.updateTimeZone(userSettings.getTimezone());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.TIMEZONE_UPDATE).build();
	}
	
	@ApiOperation(value = "verify OTP")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "verify/otp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<Boolean> otpVeriifcation(@RequestBody UserSettingsDTO userSettings) {
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.verifyCode(userSettings);
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.OTP_VERIFICATION).build();
	}
}
