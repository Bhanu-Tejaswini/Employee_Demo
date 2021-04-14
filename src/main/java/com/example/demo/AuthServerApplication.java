package com.example.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableMongoRepositories(basePackages ="com.example.demo.mongorepos")
@EnableJpaRepositories(basePackages =  "com.example.demo.repository")
public class AuthServerApplication{

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}

}
