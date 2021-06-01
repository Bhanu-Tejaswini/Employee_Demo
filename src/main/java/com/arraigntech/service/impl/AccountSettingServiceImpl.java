package com.arraigntech.service.impl;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.arraigntech.entity.User;
import com.arraigntech.exceptions.AppException;
import com.arraigntech.model.AccountSettingVO;
import com.arraigntech.model.OverLayImageVO;
import com.arraigntech.model.UserSettingsDTO;
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

	public static final Logger log = LoggerFactory.getLogger(AccountSettingServiceImpl.class);

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
		if(log.isDebugEnabled()) {
			log.debug("getTimeZonesList method start");
		}
		AccountSettingVO settingsVO = new AccountSettingVO();
		Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
		List<String> timeZonesList = new ArrayList<String>(availableZoneIds);
		settingsVO.setLanguage(UtilEnum.ENGLISH.name());
		settingsVO.setTimeZonesList(timeZonesList);
		settingsVO.setCountries(getCountiresList());
		if(log.isDebugEnabled()) {
			log.debug("getTimeZonesList method end");
		}
		return settingsVO;
	}

	private List<String> getCountiresList() {
		if(log.isDebugEnabled()) {
			log.debug("getCountiresList method start");
		}
		String[] countryCodes = Locale.getISOCountries();
		List<String> countriesList = new ArrayList<>();
		for (String countryCode : countryCodes) {
			Locale obj = new Locale("", countryCode);
			countriesList.add(obj.getDisplayCountry());
		}
		if(log.isDebugEnabled()) {
			log.debug("getCountiresList method end");
		}
		return countriesList;
	}

	@Override
	public Boolean saveUserName(String name) {
		if(log.isDebugEnabled()) {
			log.debug("saveUserName method start {}",name);
		}
		if (!StringUtils.hasText(name)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.USER_NOT_FOUND);
			}
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		User user = userService.getUser();
		User checkUser = userService.findByUsernameAndIdNot(name, user.getId());
		if (Objects.nonNull(checkUser)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.USER_EXISTS_USERNAME);
			}
			throw new AppException(MessageConstants.USER_EXISTS_USERNAME);
		}
		user.setUsername(name);
		userService.saveUser(user);
		if(log.isDebugEnabled()) {
			log.debug("saveUserName method end");
		}
		return true;
	}

	@Override
	public Boolean updateLanguage(String language) {
		if(log.isDebugEnabled()) {
			log.debug("updateLanguage method start {}",language);
		}
		if (!StringUtils.hasText(language)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.DATA_MISSING);
			}
			throw new AppException(MessageConstants.DATA_MISSING);
		}
		User user = userService.getUser();
		user.setLanguage(language);
		userService.saveUser(user);
		if(log.isDebugEnabled()) {
			log.debug("updateLanguage method end");
		}
		return true;
	}

	@Override
	public Boolean updatePincode(String pinCode) {
		if(log.isDebugEnabled()) {
			log.debug("updatePincode method start {}",pinCode);
		}

		if (!StringUtils.hasText(pinCode)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.DATA_MISSING);
			}
			throw new AppException(MessageConstants.DATA_MISSING);
		}
		User user = userService.getUser();
		user.setPincode(pinCode);
		userService.saveUser(user);
		if(log.isDebugEnabled()) {
			log.debug("updatePincode method end");
		}
		return true;
	}

	@Override
	public Boolean updateEmail(String email) {
		if(log.isDebugEnabled()) {
			log.debug("updateEmail method start {}",email);
		}
		if (!StringUtils.hasText(email)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.DATA_MISSING);
			}
			throw new AppException(MessageConstants.DATA_MISSING);
		}
		User user = userService.getUser();
		User checkUser = userService.findByEmailAndIdNot(email, user.getId());
		if (Objects.nonNull(checkUser)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.EMAIL_EXISTS);
			}
			throw new AppException(MessageConstants.EMAIL_EXISTS);
		}
		user.setEmail(email);
		userService.saveUser(user);
		if(log.isDebugEnabled()) {
			log.debug("updateEmail method end");
		}
		return true;
	}

	@Override
	public Boolean updateTimeZone(String timeZone) {
		if(log.isDebugEnabled()) {
			log.debug("updateTimeZone method start {}",timeZone);
		}
		if (!StringUtils.hasText(timeZone)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.DATA_MISSING);
			}
			throw new AppException(MessageConstants.DATA_MISSING);
		}
		User user = userService.getUser();
		user.setTimeZone(timeZone);
		userService.saveUser(user);
		if(log.isDebugEnabled()) {
			log.debug("updateTimeZone method end");
		}
		return true;
	}

	public UserSettingsDTO fetchUserSettings() {
		if(log.isDebugEnabled()) {
			log.debug("fetchUserSettings method start");
		}
		User newUser = userService.getUser();
		Map<String, String> mobileNumbersMap = new HashMap<>();
		mobileNumbersMap.put(MessageConstants.DIAL_CODE, newUser.getDialCode());
		mobileNumbersMap.put(MessageConstants.COUNTRY_CODE, newUser.getCountryCode());
		mobileNumbersMap.put(MessageConstants.E164_NUMBER, newUser.getE164Number());
		mobileNumbersMap.put(MessageConstants.NATIONAL_NUMBER, newUser.getNationalNumber());
		mobileNumbersMap.put(MessageConstants.INTERNATIONAL_NUMBER, newUser.getInternationalNumber());
		mobileNumbersMap.put(MessageConstants.NUMBER_MOBILE, newUser.getNumber());
		if(log.isDebugEnabled()) {
			log.debug("fetchUserSettings method end {}.",mobileNumbersMap);
		}
		return new UserSettingsDTO(newUser.getEmail(), newUser.getPincode(), newUser.getUsername(),
				newUser.getLanguage(), newUser.getTimeZone(), mobileNumbersMap);
	}

	@Override
	public Boolean sendOTPForUser(UserSettingsDTO userSettings) {
		if(log.isDebugEnabled()) {
			log.debug("sendOTPForUser method start {}.",userSettings);
		}
		if (!StringUtils.hasText(userSettings.getInternationalNumber())) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.INVALID_PHONE_NUMBER);
			}
			throw new AppException(MessageConstants.INVALID_PHONE_NUMBER);
		}
		User user = userService.getUser();
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
		if(log.isDebugEnabled()) {
			log.debug("sendOTPForUser method end");
		}
		return true;
	}

	@Override
	public Boolean verifyCode(UserSettingsDTO userRequest) throws AppException {
		if(log.isDebugEnabled()) {
			log.debug("verifyCode request method{}.",userRequest);
		}
		if (!StringUtils.hasText(userRequest.getCode())) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.AUTHENTICATION_FAILED);
			}
			throw new AppException(MessageConstants.AUTHENTICATION_FAILED);
		}
		User user = userService.getUser();
		Boolean isValid = verifyCode.execute(userRequest);
		if (userRequest.getCode().equalsIgnoreCase(user.getOtp())) {
			isValid = true;
		}
		if (!isValid) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.AUTHENTICATION_FAILED);
			}
			throw new AppException(MessageConstants.AUTHENTICATION_FAILED);
		} else {
			resetUserDetails.execute(user);
		}
		if(log.isDebugEnabled()) {
			log.debug("verifyCode response method {}", isValid);
		}
		return isValid;
	}

	@Override
	public Boolean verifyMobileNumber() {
		if(log.isDebugEnabled()) {
			log.debug("verifyMobileNumber method");
		}
		User newUser = userService.getUser();
		if(StringUtils.hasText(newUser.getInternationalNumber()))
			return true;
		else
			return false;
	}
}
