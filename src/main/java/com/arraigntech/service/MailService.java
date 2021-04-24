package com.arraigntech.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import com.arraigntech.model.Email;

public interface MailService {
	
	public void sendEmail(String email, String resetPasswordLink)
			throws UnsupportedEncodingException, MessagingException;
	Boolean sendEmail(Email email) throws MessagingException, IOException;
	


}
