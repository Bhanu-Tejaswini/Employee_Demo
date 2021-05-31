/**
 * 
 */
package com.arraigntech.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.arraigntech.service.MailService;

/**
 * @author Bhaskara S
 *
 */
public abstract class DependencyClass {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IVSJwtUtil jwtUtil;

	@Autowired
	private MailService mailService;
	
	@Autowired
	private EmailValidator emailValidator;

	@Autowired
	private PasswordConstraintValidator passwordValidator;

}
