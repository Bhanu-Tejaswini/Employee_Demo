package com.arraigntech.service;

import com.arraigntech.Exception.AppException;
import com.arraigntech.model.AccountSettingVO;

import com.arraigntech.model.UserSettingsDTO;

/**
 * 
 * @author tulabandula.kumar
 *
 */

public interface AccountSettingService {
	
	AccountSettingVO getTimeZonesList();

	Boolean sendOTPForUser(String mobilenumber);
	
	Boolean saveUserName(String name);
	
	Boolean updateLanguage(String language);
	
	Boolean updatePincode(String name);
	
	Boolean updateTimeZone(String name);
	
	Boolean updateMobileNumber(String name);
	
	UserSettingsDTO fetchUserSettings();
	
	Boolean verifyCode(UserSettingsDTO userRequest) throws AppException;

}
