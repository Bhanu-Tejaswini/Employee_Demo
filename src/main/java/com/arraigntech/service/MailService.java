package com.arraigntech.service;

import java.io.IOException;

import javax.mail.MessagingException;

import com.arraigntech.request.vo.EmailVO;

import freemarker.template.TemplateException;

public interface MailService {

	Boolean sendEmail(EmailVO email) throws MessagingException, IOException, TemplateException;
}
