package com.arraigntech.service.impl;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import com.arraigntech.model.UserSettingsDTO;
import com.arraigntech.repository.UserRespository;
import com.arraigntech.service.AccountSettingService;
import com.arraigntech.utility.CommonUtils;
import com.arraigntech.utility.LoggedInUserDetails;
import com.arraigntech.utility.MessageConstants;
import com.arraigntech.utility.OtpGenerator;
import com.arraigntech.utility.UtilEnum;
import com.google.common.base.Objects;
import com.twilio.Twilio;

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
	
	@Value("${app.mobile.otp.validity}")
	protected int mobileOTPExpiryTime;
	
	@Value("${sponsor.sms.OTPLength:4}")
	private Integer sponsorSmsOTPLength;
	
	@Value("${twilio.account.id}")
	private String twilioAccountId;
	
	@Value("${twilio.auth.token}")
	private String twilioAccessToken;
	
	@Autowired
	private UserRespository userRepo;
	
	private final static String ACCOUNT_SID = "<ACCOUNT-SID>";
	private final static String AUTH_ID = "AUTH-ID";

//	   static {
//	      Twilio.init(twilioAccountId, twilioAccessToken);
//	   }

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
	public Boolean sendOTPForUser(String mobilenumber) {
		// TODO Auto-generated method stub
		if (!StringUtils.hasText(mobilenumber)) {
			throw new AppException(MessageConstants.INVALID_PHONE_NUMBER);
		}
		// generate OTP
		String otp = otpGenerator.generateOTP(sponsorSmsOTPLength);
		Twilio.init(twilioAccountId, twilioAccessToken);
		// send SMS OTP
		//sendSMSOTP(mobilenumber, otp);
		return null;
	}

	@Override
	public Boolean saveUserName(String name) {
		User user = getUser();
		user.setUsername(name);
		userRepo.save(user);
		return true;
	}

	@Override
	public Boolean updateLanguage(String language) {
		User user = getUser();
		user.setLanguage(language);
		userRepo.save(user);
		return true;
	}

	@Override
	public Boolean updatePincode(String pinCode) {
		User user = getUser();
		user.setPincode(pinCode);
		userRepo.save(user);
		return true;
		
	}

	@Override
	public Boolean updateTimeZone(String timeZone) {
		User user = getUser();
		user.setTimeZone(timeZone);
		userRepo.save(user);
		return true;
	}

	@Override
	public Boolean updateMobileNumber(String mobileNumber) {
		User user = getUser();
		user.setMobileNumber(mobileNumber);
		userRepo.save(user);
		return true;
	}
	
//	protected void sendSMSOTP(String phoneNumber, RegionEnum region, String otp) {
//		notificationService.sendSMS(phoneNumber, region, otp);
//	}
	
	public UserSettingsDTO fetchUserSettings() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		Optional<User> optionalUser=userRepo.findByUsername(authentication.getName());
		Optional<User> optionalUser=userRepo.findByUsername("user1");
		if(!optionalUser.isPresent()) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		User newUser=optionalUser.get();
		return new UserSettingsDTO(newUser.getEmail(),newUser.getMobileNumber(),newUser.getPincode(),newUser.getUsername(),
				newUser.getLanguage(),newUser.getTimeZone());
	}
	
	private User getUser() {
		LoggedInUserDetails userDetails = CommonUtils.getUser();
		User user = userRepo.findByEmail(userDetails.getEmail());
		if(user == null) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		return user;
	}

}
