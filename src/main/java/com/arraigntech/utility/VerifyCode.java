package com.arraigntech.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.arraigntech.exceptions.AppException;
import com.arraigntech.request.UserSettingsVO;

@Component
public class VerifyCode implements Action<UserSettingsVO, Boolean> {
	
	public static final Logger log = LoggerFactory.getLogger(VerifyCode.class);

	@Override
	public Boolean execute(UserSettingsVO input) throws AppException {
		log.debug("execute method start");
//		if (input.getCode().equals(Twilio.)) {
//			return Boolean.TRUE;
//		}
		log.debug("execute method start");
		return Boolean.FALSE;
	}

}
