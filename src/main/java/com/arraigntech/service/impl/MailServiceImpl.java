package com.arraigntech.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.arraigntech.model.Email;
import com.arraigntech.service.MailService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class MailServiceImpl implements MailService {

	public static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Configuration freemarkerConfig;

	@Override
	public Boolean sendEmail(Email email) throws MessagingException, IOException, TemplateException {
		if(log.isDebugEnabled()) {
			log.debug("Sending email to {}", email.getTo());
		}
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());
		// MimeMessageHelper helper = new MimeMessageHelper(message);
		Map model = email.getMessageBody();

		Template template = freemarkerConfig.getTemplate(email.getTemplateName());
		String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		helper.setTo(email.getTo());
		helper.setText(html, true);
		helper.setSubject(email.getSubject());
		helper.setFrom(email.getFrom(), "Vstreem Support");
		mailSender.send(message);
		if(log.isDebugEnabled()) {
			log.debug("Sending email to {} ended", email.getTo());
		}
		return Boolean.TRUE;
	}
}
