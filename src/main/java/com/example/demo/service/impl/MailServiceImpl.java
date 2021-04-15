package com.example.demo.service.impl;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.service.MailService;

@Service
public class MailServiceImpl implements MailService{

	@Autowired
	private JavaMailSender mailSender;

	static final String FROM = "bhaskaras1999@gmail.com";

	static final String SUBJECT = "The Link to reset your password";

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
}
