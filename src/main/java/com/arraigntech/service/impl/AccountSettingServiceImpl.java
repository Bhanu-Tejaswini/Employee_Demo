package com.arraigntech.service.impl;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.arraigntech.entity.UserEntity;
import com.arraigntech.exceptions.AppException;
import com.arraigntech.request.vo.AccountSettingVO;
import com.arraigntech.request.vo.UserSettingsVO;
import com.arraigntech.service.AccountSettingService;
import com.arraigntech.service.UserService;
import com.arraigntech.utility.MessageConstants;
import com.arraigntech.utility.OtpGenerator;
import com.arraigntech.utility.ResetUserDetails;
import com.arraigntech.utility.UtilEnum;
import com.arraigntech.utility.VerifyCode;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

/**
 * 
 * @author tulabandula.kumar
 *
 */
@Service
public class AccountSettingServiceImpl implements AccountSettingService {

	@Autowired
	protected OtpGenerator otpGenerator;

	@Value("${app.mobile.otp.validity:5}")
	protected int mobileOTPExpiryTime;

	@Value("${sponsor.sms.OTPLength:4}")
	private Integer userSmsOTPLength;

	@Value("${twilio.account.id}")
	private String twilioAccountId;

	@Value("${twilio.auth.token}")
	private String twilioAccessToken;

	@Value("${twilio.phone.number}")
	private String twilioPhoneNumber;

	@Autowired
	private UserService userService;

	@Autowired
	protected VerifyCode verifyCode;

	@Autowired
	protected ResetUserDetails resetUserDetails;

	@Override
	public AccountSettingVO getTimeZonesList() {
		AccountSettingVO settingsVO = new AccountSettingVO();
		Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
		List<String> timeZonesList = new ArrayList<String>(availableZoneIds);
		settingsVO.setLanguage(UtilEnum.ENGLISH.name());
		settingsVO.setTimeZonesList(timeZonesList);
		settingsVO.setCountries(getCountiresList());
		return settingsVO;
	}

	private List<String> getCountiresList() {
		String[] countryCodes = Locale.getISOCountries();
		List<String> countriesList = new ArrayList<>();
		for (String countryCode : countryCodes) {
			Locale obj = new Locale("", countryCode);
			countriesList.add(obj.getDisplayCountry());
		}
		return countriesList;
	}

	@Override
	public Boolean saveUserName(String name) {
		if (!StringUtils.hasText(name)) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		UserEntity user = userService.getUser();
		UserEntity checkUser = userService.findByUsernameAndIdNot(name, user.getId());
		if (Objects.nonNull(checkUser)) {
			throw new AppException(MessageConstants.USER_EXISTS_USERNAME);
		}
		user.setUsername(name);
		userService.saveUser(user);
		return true;
	}

	@Override
	public Boolean updateLanguage(String language) {
		if (!StringUtils.hasText(language)) {
			throw new AppException(MessageConstants.DATA_MISSING);
		}
		UserEntity user = userService.getUser();
		user.setLanguage(language);
		userService.saveUser(user);
		return true;
	}

	@Override
	public Boolean updatePincode(String pinCode) {
		if (!StringUtils.hasText(pinCode)) {
			throw new AppException(MessageConstants.DATA_MISSING);
		}
		UserEntity user = userService.getUser();
		user.setPincode(pinCode);
		userService.saveUser(user);
		return true;
	}

	@Override
	public Boolean updateEmail(String email) {
		if (!StringUtils.hasText(email)) {
			throw new AppException(MessageConstants.DATA_MISSING);
		}
		UserEntity user = userService.getUser();
		UserEntity checkUser = userService.findByEmailAndIdNot(email, user.getId());
		if (Objects.nonNull(checkUser)) {
			throw new AppException(MessageConstants.EMAIL_EXISTS);
		}
		user.setEmail(email);
		userService.saveUser(user);
		return true;
	}

	@Override
	public Boolean updateTimeZone(String timeZone) {
		if (!StringUtils.hasText(timeZone)) {
			throw new AppException(MessageConstants.DATA_MISSING);
		}
		UserEntity user = userService.getUser();
		user.setTimeZone(timeZone);
		userService.saveUser(user);
		return true;
	}

	public UserSettingsVO fetchUserSettings() {
		UserEntity newUser = userService.getUser();
		Map<String, String> mobileNumbersMap = new HashMap<>();
		mobileNumbersMap.put(MessageConstants.DIAL_CODE, newUser.getDialCode());
		mobileNumbersMap.put(MessageConstants.COUNTRY_CODE, newUser.getCountryCode());
		mobileNumbersMap.put(MessageConstants.E164_NUMBER, newUser.getE164Number());
		mobileNumbersMap.put(MessageConstants.NATIONAL_NUMBER, newUser.getNationalNumber());
		mobileNumbersMap.put(MessageConstants.INTERNATIONAL_NUMBER, newUser.getInternationalNumber());
		mobileNumbersMap.put(MessageConstants.NUMBER_MOBILE, newUser.getNumber());
		return new UserSettingsVO(newUser.getEmail(), newUser.getPincode(), newUser.getUsername(),
				newUser.getLanguage(), newUser.getTimeZone(), mobileNumbersMap);
	}

	@Override
	public Boolean sendOTPForUser(UserSettingsVO userSettings) {
		if (!StringUtils.hasText(userSettings.getInternationalNumber())) {
			throw new AppException(MessageConstants.INVALID_PHONE_NUMBER);
		}
		UserEntity user = userService.getUser();
		// generate OTP
		user.setDialCode(userSettings.getDialCode());
		user.setCountryCode(userSettings.getCountryCode());
		user.setInternationalNumber(userSettings.getInternationalNumber());
		user.setE164Number(userSettings.getE164Number());
		user.setNationalNumber(userSettings.getNationalNumber());
		user.setNumber(userSettings.getNumber());
		String otp = otpGenerator.generateOTP(userSmsOTPLength);
		user.setOtp(otp);
		Twilio.init(twilioAccountId, twilioAccessToken);
		Message.creator(new com.twilio.type.PhoneNumber(userSettings.getInternationalNumber()), // The phone number you
																								// are sending text to
				new com.twilio.type.PhoneNumber(twilioPhoneNumber), // The Twilio phone number
				"Please enter the OTP:" + otp).create();
		userService.saveUser(user);
		return true;
	}

	@Override
	public Boolean verifyCode(UserSettingsVO userRequest) throws AppException {
		if (!StringUtils.hasText(userRequest.getCode())) {
			throw new AppException(MessageConstants.AUTHENTICATION_FAILED);
		}
		UserEntity user = userService.getUser();
		Boolean isValid = verifyCode.execute(userRequest);
		if (userRequest.getCode().equalsIgnoreCase(user.getOtp())) {
			isValid = true;
		}
		if (!isValid) {
			throw new AppException(MessageConstants.AUTHENTICATION_FAILED);
		} else {
			resetUserDetails.execute(user);
		}
		return isValid;
	}

	@Override
	public Boolean verifyMobileNumber() {
		UserEntity newUser = userService.getUser();
		if(StringUtils.hasText(newUser.getInternationalNumber()))
			return true;
		else
			return false;
	}
}
