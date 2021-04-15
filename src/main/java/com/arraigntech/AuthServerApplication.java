package com.arraigntech;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableMongoRepositories(basePackages ="com.arraigntech.mongorepos")
@EnableJpaRepositories(basePackages =  "com.arraigntech.repository")
public class AuthServerApplication{

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}

}
