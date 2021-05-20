package com.arraigntech.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

	static final String FROM = "bhaskaras1999@gmail.com";

	static final String SUBJECT = "The Link to reset your password";
	
	@Autowired
	private Configuration freemarkerConfig;

	public void sendEmail(String email, String resetPasswordLink)
			throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(FROM, "Vstream Support");
		helper.setTo(email);
		helper.setSubject(SUBJECT);

		String content = "<p> Hello,</p>" + "<p>You have requested to reset the Vstream password.</p>"
				+ "<p>Click on the below link to reset your password</p>" + "<p><b><a href=\"" + resetPasswordLink
				+ "\">Change my passsword here</a></b></p>";
		helper.setText(content, true);
		mailSender.send(message);

	}

	@Override
	public Boolean sendEmail(Email email) throws MessagingException, IOException, TemplateException {
		log.debug("Sending email to {}", email.getTo());
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());
		//MimeMessageHelper helper = new MimeMessageHelper(message);
		Map model = email.getMessageBody();

		Template template = freemarkerConfig.getTemplate(email.getTemplateName());
		String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		helper.setTo(email.getTo());
		helper.setText(html, true);
		helper.setSubject(email.getSubject());
		helper.setFrom(email.getFrom(),"Vstream Support");
		mailSender.send(message);
		log.debug("Sending email to {} ended", email.getTo());
		return Boolean.TRUE;
	}
}
