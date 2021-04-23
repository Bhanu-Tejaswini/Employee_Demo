package com.arraigntech.utility;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.arraigntech.model.Email;

@Component
public class FormEmailData {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FormEmailData.class);

	public Email formEmail(String fromEmail, String toEmail, String subject, Map model) {
		LOGGER.debug("Started preparing the email data");
		Email email = new Email();
		email.setFrom(fromEmail);
		email.setTo(toEmail);
		email.setSubject(subject);
		email.setMessageBody(model);
		return email;
	}



}
