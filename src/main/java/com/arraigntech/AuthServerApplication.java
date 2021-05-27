package com.arraigntech;

import java.util.Date;
import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import com.arraigntech.repository.StreamRepository;
import com.arraigntech.service.impl.IVSStreamServiceImpl;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableAsync
//@EnableScheduling
@EnableMongoRepositories(basePackages = "com.arraigntech.mongorepos")
@EnableJpaRepositories(basePackages = "com.arraigntech.repository")
public class AuthServerApplication {

	public static final Logger log = LoggerFactory.getLogger(IVSStreamServiceImpl.class);

	@Bean
	public Executor workExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setThreadNamePrefix("Async-");
		threadPoolTaskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
		threadPoolTaskExecutor.setMaxPoolSize(10);
		threadPoolTaskExecutor.setQueueCapacity(600);
		threadPoolTaskExecutor.afterPropertiesSet();
		threadPoolTaskExecutor.initialize();
		log.info("ThreadPoolTaskExecutor set");
		return threadPoolTaskExecutor;
	}

	@PostConstruct
	void setGlobalSecurityContext() {
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
	}

	@Primary
	@Bean
	public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
		FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
		bean.setTemplateLoaderPath("classpath:/templates");
		return bean;
	}

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}
}
