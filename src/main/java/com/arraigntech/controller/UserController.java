package com.arraigntech.controller;

import java.net.HttpURLConnection;

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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/account")
public class UserController {
	
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
		return new BaseResponse<UserSettingsDTO>(userService.fetchUserSettings()).withSuccess(true);
	}
	
	@ApiOperation(value = "save user Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/user-settings", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<String> postUserSettings(@RequestBody UserSettingsDTO userSettings) {
		return new BaseResponse<String>(userService.saveUserSettings(userSettings)).withSuccess(true);
	}
	
	@ApiOperation(value = "timezonelist and country list")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/timezonelist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public BaseResponse<AccountSettingVO> getTimeZonesList() {
		return new BaseResponse<AccountSettingVO>(accountSettingService.getTimeZonesList()).withSuccess(true);
	}
}
