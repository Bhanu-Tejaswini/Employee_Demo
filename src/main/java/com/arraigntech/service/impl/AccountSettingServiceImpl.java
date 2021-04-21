package com.arraigntech.service.impl;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.arraigntech.model.AccountSettingVO;
import com.arraigntech.service.AccountSettingService;
import com.arraigntech.utility.UtilEnum;

/**
 * 
 * @author tulabandula.kumar
 *
 */
@Service
public class AccountSettingServiceImpl implements AccountSettingService {
	
	public static final Logger log = LoggerFactory.getLogger(AccountSettingServiceImpl.class);

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

}
