package com.employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AuthServerApplication {

	public static final Logger logger = LoggerFactory.getLogger(AuthServerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
		logger.info("Application Started");
	}
}
