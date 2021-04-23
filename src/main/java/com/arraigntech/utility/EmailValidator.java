package com.arraigntech.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailValidator {
	@Value("${app.customer.emailRegex:^[A-Za-z0-9+!#$%&'*+-/=?^_`{|}]+@(.+)$}")
	private String emailRegex;
	private Pattern pattern;

	@PostConstruct
	private void onInit() {
		pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
	}

	public boolean isValidEmail(String email) {
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
}
