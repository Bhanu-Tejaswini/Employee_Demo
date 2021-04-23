package com.arraigntech.service.impl;


import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.arraigntech.Exception.AppException;
import com.arraigntech.entity.User;
import com.arraigntech.model.AccountSettingVO;
import com.arraigntech.model.AuthUserDetail;
import com.arraigntech.model.UserSettingsDTO;
import com.arraigntech.repository.UserRespository;
import com.arraigntech.service.AccountSettingService;
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
	
//	@Value("${app.mobile.otp.validity}")
//	protected int mobileOTPExpiryTime;
	
	@Value("${sponsor.sms.OTPLength:4}")
	private Integer userSmsOTPLength;
	
	@Value("${twilio.account.id}")
	private static String twilioAccountId;
	
	@Value("${twilio.auth.token}")
	private static String twilioAccessToken;
	
	@Autowired
	private UserRespository userRepo;
	
	@Autowired
	protected VerifyCode verifyCode;
	
	@Autowired
	protected ResetUserDetails resetUserDetails;
	


	@Override
	public AccountSettingVO getTimeZonesList() {
		log.debug("getTimeZonesList method start");
		AccountSettingVO settingsVO = new AccountSettingVO();
		Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
		List<String> timeZonesList = new ArrayList<String>(availableZoneIds);
		settingsVO.setLanguage(UtilEnum.ENGLISH.name());
		settingsVO.setTimeZonesList(timeZonesList);
		settingsVO.setCountries(getCountiresList());
		log.debug("getTimeZonesList method end");
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
		if(!StringUtils.hasText(name)) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		User user = getUser();
		User checkUser=userRepo.findByUsernameAndIdNot(name, user.getId());
		if(Objects.nonNull(checkUser)) {
			throw new AppException(MessageConstants.USER_EXISTS_USERNAME);
		}
		user.setUsername(name);
		userRepo.save(user);
		return true;
	}

	@Override
	public Boolean updateLanguage(String language) {
		if(!StringUtils.hasText(language)) {
			throw new AppException(MessageConstants.DATA_MISSING);
		}
		User user = getUser();
		user.setLanguage(language);
		userRepo.save(user);
		return true;
	}

	@Override
	public Boolean updatePincode(String pinCode) {
		if(!StringUtils.hasText(pinCode)) {
			throw new AppException(MessageConstants.DATA_MISSING);
		}
		User user = getUser();
		user.setPincode(pinCode);
		userRepo.save(user);
		return true;
	}
	
	@Override
	public Boolean updateEmail(String email) {
		if(!StringUtils.hasText(email)) {
			throw new AppException(MessageConstants.DATA_MISSING);
		}
		User user = getUser();
		User checkUser=userRepo.findByEmailAndIdNot(email, user.getId());
		if(Objects.nonNull(checkUser)) {
			throw new AppException(MessageConstants.EMAIL_EXISTS);
		}
		user.setEmail(email);
		userRepo.save(user);
		return true;
	}

	@Override
	public Boolean updateTimeZone(String timeZone) {
		if(!StringUtils.hasText(timeZone)) {
			throw new AppException(MessageConstants.DATA_MISSING);
		}
		User user = getUser();
		user.setTimeZone(timeZone);
		userRepo.save(user);
		return true;
	}

	@Override
	public Boolean updateMobileNumber(String mobileNumber) {
		if(!StringUtils.hasText(mobileNumber)) {
			throw new AppException(MessageConstants.DATA_MISSING);
		}
		User user = getUser();
		user.setMobileNumber(mobileNumber);
		userRepo.save(user);
		return true;
	}
	
	public UserSettingsDTO fetchUserSettings() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> optionalUser=userRepo.findByUsername(authentication.getName());
		if(!optionalUser.isPresent()) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		User newUser=optionalUser.get();
		return new UserSettingsDTO(newUser.getEmail(),newUser.getMobileNumber(),newUser.getPincode(),newUser.getUsername(),
				newUser.getLanguage(),newUser.getTimeZone());
	}
	
	private User getUser() {
		
		String username=SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> user = userRepo.findByUsername(username);
		if(user == null) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		return user.get();
	}
	
	@Override
	public Boolean sendOTPForUser(String mobilenumber) {
		if (!StringUtils.hasText(mobilenumber)) {
			throw new AppException(MessageConstants.INVALID_PHONE_NUMBER);
		}
		User user = getUser();
		// generate OTP
		String otp = otpGenerator.generateOTP(userSmsOTPLength);
		user.setOtp(otp);
		Twilio.init(twilioAccountId, twilioAccessToken);
		Message.creator(
		                new com.twilio.type.PhoneNumber(mobilenumber),//The phone number you are sending text to
		                new com.twilio.type.PhoneNumber("+31251121"),//The Twilio phone number
		                "Please enter the OTP:" +otp)
		           .create();
		userRepo.save(user);
		return true;
	}
	
	@Override
	public Boolean verifyCode(UserSettingsDTO userRequest) throws AppException {
		log.debug("verifyCode request{}");
		if (!StringUtils.hasText(userRequest.getCode())) {
			throw new AppException(MessageConstants.AUTHENTICATION_FAILED);
		}
		User user = getUser();
		Boolean isValid = verifyCode.execute(userRequest);
		if (!isValid) {
			throw new AppException(MessageConstants.AUTHENTICATION_FAILED);
		} else {
			resetUserDetails.execute(user);
		}
		log.debug("verifyCode response{}", isValid);
		return isValid;
	}

}
