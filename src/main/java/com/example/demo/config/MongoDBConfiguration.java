package com.example.demo.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoDBConfiguration {
	
//	@Value("${mongodb.url}")
//	private String dbUrl;
//	
//	@Value("${mongodb.db}")
//	private String dbname;
	
	 	@Bean
	    public MongoClient mongo() {
	        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/vstream");
	        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
	          .applyConnectionString(connectionString)
	          .build();
	        
	        return MongoClients.create(mongoClientSettings);
	    }

	    @Bean
	    public MongoTemplate mongoTemplate() throws Exception {
	        return new MongoTemplate(mongo(), "vstream");
	    }
}
