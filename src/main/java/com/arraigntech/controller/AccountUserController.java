package com.arraigntech.controller;

import java.net.HttpURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arraigntech.exceptions.AppException;
import com.arraigntech.request.EmailSettingsVO;
import com.arraigntech.request.IVSPasswordVO;
import com.arraigntech.request.IVSResetPasswordVO;
import com.arraigntech.request.UserSettingsVO;
import com.arraigntech.response.BaseResponse;
import com.arraigntech.service.AccountSettingService;
import com.arraigntech.service.DocumentS3Service;
import com.arraigntech.service.impl.UserServiceImpl;
import com.arraigntech.utility.MessageConstants;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/account")
public class AccountUserController {

	public static final String MOBILENUMBER_VERIFIED = "MobileNumberVerified";

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	protected AccountSettingService accountSettingService;
	
	@Autowired
	private DocumentS3Service s3Service;
	
	
	@Value("${mutlipartfile.size}")
	private long mutliPartFileSize;
	

	@ApiOperation(value = "Update password")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/update-password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> resetPassword(@RequestBody IVSPasswordVO pass) {
		return new BaseResponse<String>(userService.updateAccountPassword(pass.getPassword(), pass.getNewpassword()))
				.withSuccess(true);
	}

	@ApiOperation(value = "Fetch email Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/email-settings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<EmailSettingsVO> getEmailSettings() {
		return new BaseResponse<EmailSettingsVO>(userService.getEmailSetting()).withSuccess(true);
	}

	@ApiOperation(value = "save email Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/email-settings", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> postEmailSettings(@RequestBody EmailSettingsVO emailSettingsModel) {
		return new BaseResponse<String>(userService.saveEmailSettings(emailSettingsModel)).withSuccess(true);
	}

	@ApiOperation(value = "Fetch user Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/user-settings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<UserSettingsVO> getUserSettings() {
		return new BaseResponse<UserSettingsVO>(accountSettingService.fetchUserSettings()).withSuccess(true);
	}

	@ApiOperation(value = "send a OTP for mobile number verification")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/sendOTP", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> sendOTPForUser(@RequestBody UserSettingsVO userSettings) {
		return new BaseResponse<Boolean>(accountSettingService.sendOTPForUser(userSettings)).withSuccess(true);
	}

	@ApiOperation(value = "save userName Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "username/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> userNameUpdate(@RequestBody UserSettingsVO userSettings) {
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.saveUserName(userSettings.getUsername());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.USERNAME_UPDATE).build();
	}

	@ApiOperation(value = "save email Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "email/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> emailUpdate(@RequestBody UserSettingsVO userSettings) {
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.updateEmail(userSettings.getEmail());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.EMAILSETTINGSMESSAGE).build();
	}

	@ApiOperation(value = "save pincode Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "pincode/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> pincodeUpdate(@RequestBody UserSettingsVO userSettings) {
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.updatePincode(userSettings.getPincode());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.PINCODE_UPDATE).build();
	}

	@ApiOperation(value = "save language Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "language/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> languageUpdate(@RequestBody UserSettingsVO userSettings) {
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.updateLanguage(userSettings.getLanguage());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.LANGUAGE_UPDATE).build();
	}

	@ApiOperation(value = "save timeZone Settings")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "timeZone/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> timeZoneUpdate(@RequestBody UserSettingsVO userSettings) {
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.updateTimeZone(userSettings.getTimezone());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.TIMEZONE_UPDATE).build();
	}

	@ApiOperation(value = "verify OTP")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "verify/otp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> otpVeriifcation(@RequestBody UserSettingsVO userSettings) {
		BaseResponse<Boolean> response = new BaseResponse<>();
		accountSettingService.verifyCode(userSettings);
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.OTP_VERIFICATION).build();
	}

	@ApiOperation(value = "Delete user account")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "/user", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> deleteUser(@RequestBody IVSResetPasswordVO password) {
		BaseResponse<Boolean> response = new BaseResponse<>();
		userService.delete(password.getPassword());
		return response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, MessageConstants.USER_DELETED).build();
	}

	@ApiOperation(value = "verify mobile number")
	@ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "On success response") })
	@RequestMapping(value = "verify/mobilenumber", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<Boolean> mobilenumberVerification() {
		BaseResponse<Boolean> response = new BaseResponse<>();
		Boolean result = accountSettingService.verifyMobileNumber();
		return result ? response.withSuccess(true).withResponseMessage(MessageConstants.KEY_SUCCESS, "true")
				: response.withSuccess(true).withResponseMessage(MessageConstants.KEY_SUCCESS, "false");
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/upload/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public BaseResponse<String> uploadFile(@RequestParam("file") MultipartFile file, String type) {
		BaseResponse<String> response = new BaseResponse<>();
		if(file.getSize() > (mutliPartFileSize * 1024 * 1024)) {
			throw new AppException(MessageConstants.FILE_SIZE_ERROR);
		}
		String documentPath = s3Service.uploadFile(file);
		s3Service.saveAWSDocumentDetails(type, documentPath);
		return documentPath != null ? response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, "file uploaded successfully") :
					response.withSuccess(true)
					.withResponseMessage(MessageConstants.KEY_SUCCESS, "Exception in uploading the file to S3");
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{fileUrl}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> deleteFile(@PathVariable String path) {
		BaseResponse<String> response = new BaseResponse<>();
		Boolean documentPath = s3Service.deleteFileFromS3Bucket(path);
		return documentPath != null ? response.withSuccess(true)
				.withResponseMessage(MessageConstants.KEY_SUCCESS, "file deleted successfully") :
					response.withSuccess(true)
					.withResponseMessage(MessageConstants.KEY_SUCCESS, "Exception in deleting the file to S3");
	}
}
