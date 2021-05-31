package com.arraigntech.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.arraigntech.exceptions.AppException;
import com.arraigntech.model.UserSettingsDTO;

@Component
public class VerifyCode implements Action<UserSettingsDTO, Boolean> {
	
	public static final Logger log = LoggerFactory.getLogger(VerifyCode.class);

	@Override
	public Boolean execute(UserSettingsDTO input) throws AppException {
		log.debug("execute method start");
//		if (input.getCode().equals(Twilio.)) {
//			return Boolean.TRUE;
//		}
		log.debug("execute method start");
		return Boolean.FALSE;
	}

}
