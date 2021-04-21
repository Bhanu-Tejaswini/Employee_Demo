package com.arraigntech.utility;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OtpGenerator {


	public static final Logger log = LoggerFactory.getLogger(OtpGenerator.class);


	private SecureRandom random;
	
	@Value("${is_test_otp:true}")
	private boolean isTestOTP;

	@PostConstruct
	private void onInit() throws NoSuchAlgorithmException {
		random = new SecureRandom();
	}

	public String generateOTP(int length) {

		if (isTestOTP)
			return "1234";

		log.debug("Generate otp numbers");
		return generateRandom(length);

	}

	public String generateRandom(int length) {
		String numbers = "1234567890";

		char[] otp = new char[length];

		for (int i = 0; i < length; i++) {
			otp[i] = numbers.charAt(random.nextInt(numbers.length()));
		}
		return String.valueOf(otp);
	}



}
