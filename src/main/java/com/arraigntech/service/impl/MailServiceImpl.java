package com.arraigntech.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.arraigntech.request.EmailVO;
import com.arraigntech.service.MailService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Configuration freemarkerConfig;

	@Override
	public Boolean sendEmail(EmailVO email) throws MessagingException, IOException, TemplateException {
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
		return Boolean.TRUE;
	}
}
