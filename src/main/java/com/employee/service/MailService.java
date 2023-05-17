package com.employee.service;

import java.io.IOException;

import javax.mail.MessagingException;

import com.employee.request.vo.EmailVO;


public interface MailService {

	Boolean sendEmail(EmailVO email) throws MessagingException, IOException;
}
