package com.arraigntech.service;

import java.io.IOException;

import javax.mail.MessagingException;

import com.arraigntech.model.Email;

import freemarker.template.TemplateException;

public interface MailService {

	Boolean sendEmail(Email email) throws MessagingException, IOException, TemplateException;
}
