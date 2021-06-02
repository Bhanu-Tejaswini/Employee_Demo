package com.arraigntech.service;

import com.arraigntech.exceptions.AppException;
import com.arraigntech.request.AccountSettingVO;
import com.arraigntech.request.OverLayImageVO;
import com.arraigntech.request.UserSettingsVO;

/**
 * 
 * @author tulabandula.kumar
 *
 */

public interface AccountSettingService {

	AccountSettingVO getTimeZonesList();

	Boolean sendOTPForUser(UserSettingsVO userSettings);

	Boolean saveUserName(String name);

	Boolean updateLanguage(String language);

	Boolean updatePincode(String name);

	Boolean updateTimeZone(String name);

	Boolean verifyMobileNumber();

	Boolean updateEmail(String email);

	UserSettingsVO fetchUserSettings();

	Boolean verifyCode(UserSettingsVO userRequest) throws AppException;

}
