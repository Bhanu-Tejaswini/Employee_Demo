package com.arraigntech.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

public interface MailService {
	
	public void sendEmail(String email, String resetPasswordLink)
			throws UnsupportedEncodingException, MessagingException;

}
