package com.employee.utility;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.employee.request.vo.EmailVO;

@Component
public class FormEmailData {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FormEmailData.class);

	public EmailVO formEmail(String fromEmail, String toEmail, String subject, String registrationToken,
			String templateName, Map model) {
		LOGGER.debug("Started preparing the email data");
		EmailVO email = new EmailVO();
		email.setFrom(fromEmail);
		email.setTo(toEmail);
		email.setSubject(subject);
		email.setMessageBody(model);
		email.setTemplateName(templateName);
		return email;
	}

}
