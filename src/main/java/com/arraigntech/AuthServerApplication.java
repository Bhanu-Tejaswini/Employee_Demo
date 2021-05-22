package com.arraigntech;


import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.arraigntech.service.impl.IVSStreamServiceImpl;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableAsync
@EnableMongoRepositories(basePackages ="com.arraigntech.mongorepos")
@EnableJpaRepositories(basePackages =  "com.arraigntech.repository")
public class AuthServerApplication{
	
	public static final Logger log = LoggerFactory.getLogger(IVSStreamServiceImpl.class);

	@Bean
    public Executor workExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("Async-");
        threadPoolTaskExecutor.setCorePoolSize(3);
        threadPoolTaskExecutor.setMaxPoolSize(3);
        threadPoolTaskExecutor.setQueueCapacity(600);
        threadPoolTaskExecutor.afterPropertiesSet();
        log.info("ThreadPoolTaskExecutor set");
        return threadPoolTaskExecutor;
    }
	
	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}
}
