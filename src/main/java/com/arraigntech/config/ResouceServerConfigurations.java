
package com.arraigntech.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import com.arraigntech.service.impl.CustomOauth2UserService;

@Configuration
@EnableResourceServer
public class ResouceServerConfigurations extends ResourceServerConfigurerAdapter {

	@Autowired
	private ResourceServerTokenServices tokenServices;

	@Value("${security.jwt.resource-ids}")
	private String resourceIds;
	
	@Autowired
	private CustomOauth2UserService oauth2UserService;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(resourceIds);
//		.tokenServices(tokenServices);
	}


	@Override
	public void configure(HttpSecurity http) throws Exception {
		System.out.println("resource sec starts");
		http.csrf().disable();
		http.cors().and()
        .requestMatchers()
        .and()
        .authorizeRequests()
        .antMatchers("/actuator/**", "v2/api-docs/**", "/oauth/*", "/auth/**").permitAll()
//        .anyRequest().authenticated()
        .and()
        .oauth2Login()
//        	.defaultSuccessUrl("/prevent",true)
        .permitAll()
    	.userInfoEndpoint().userService(oauth2UserService);
//        .and().sessionManagement()
//        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        .antMatchers("/user").hasAnyAuthority("create_profile").anyRequest().authenticated();
		
	}
	
}
