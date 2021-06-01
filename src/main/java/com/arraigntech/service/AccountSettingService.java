package com.arraigntech.service;

import com.arraigntech.exceptions.AppException;
import com.arraigntech.model.AccountSettingVO;
import com.arraigntech.model.OverLayImageVO;
import com.arraigntech.model.UserSettingsDTO;

/**
 * 
 * @author tulabandula.kumar
 *
 */

public interface AccountSettingService {

	AccountSettingVO getTimeZonesList();

	Boolean sendOTPForUser(UserSettingsDTO userSettings);

	Boolean saveUserName(String name);

	Boolean updateLanguage(String language);

	Boolean updatePincode(String name);

	Boolean updateTimeZone(String name);

	Boolean verifyMobileNumber();

	Boolean updateEmail(String email);

	UserSettingsDTO fetchUserSettings();

	Boolean verifyCode(UserSettingsDTO userRequest) throws AppException;

}
