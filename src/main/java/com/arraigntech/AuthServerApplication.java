package com.arraigntech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableAsync
@EnableScheduling
@EnableMongoRepositories(basePackages = "com.arraigntech.mongorepos")
@EnableJpaRepositories(basePackages = "com.arraigntech.repository")
public class AuthServerApplication {

	public static final Logger logger = LoggerFactory.getLogger(AuthServerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
		logger.info("Application Started");
	}
}
