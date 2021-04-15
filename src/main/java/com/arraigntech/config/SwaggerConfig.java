package com.arraigntech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(getApiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.example.demo")).paths(PathSelectors.any()).build();

	}

	private ApiInfo getApiInfo() {
		return new ApiInfoBuilder().title("Vstream API").description("This page lists all API's of User Registration")
				.version("1.0").contact(new Contact("Bhaskar", "https://arraigntech.com/", "bhaskar@arraigntech.com"))
				.license("License 1.0").licenseUrl("https://arraigntech.com/").build();
	}

}