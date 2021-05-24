
package com.arraigntech.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableResourceServer
public class ResouceServerConfigurations extends ResourceServerConfigurerAdapter {
	
	public final static String CSRF_HEADER_NAME =  "Access-Control-Allow-Origin";

	@Autowired
	private ResourceServerTokenServices tokenServices;

	@Value("${security.jwt.resource-ids}")
	private String resourceIds;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(resourceIds);
//		.tokenServices(tokenServices);
	}

//	@Bean
//	public CorsFilter corsFilter() {
//	  UrlBasedCorsConfigurationSource source = new 
//	  UrlBasedCorsConfigurationSource();
//	  CorsConfiguration config = new CorsConfiguration();
//	  config.setAllowCredentials(true);
//	  config.addAllowedOriginPattern("*");
//	  config.addAllowedHeader("*");
//	  config.addAllowedMethod("*");
//	  source.registerCorsConfiguration("/**", config);
//	  return new CorsFilter(source);
//	}
	
	@Bean("corsFilter")
	public CorsFilter corsFilter() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOriginPattern("*");
		config.addAllowedHeader("*");
		config.addExposedHeader(CSRF_HEADER_NAME);
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("HEAD");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("DELETE");
		config.addAllowedMethod("PATCH");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.cors().and()
        .requestMatchers()
        .and()
        .authorizeRequests()
        .antMatchers("/actuator/**", "v2/api-docs/**", "/oauth/**","/oauth2/**", "/auth/**","/timezonelist","/images/**").permitAll()
        .anyRequest().authenticated()
//        .and()
//        .oauth2Login()
////        	.defaultSuccessUrl("/prevent",true),
//        .permitAll()
//    	.userInfoEndpoint().userService(oauth2UserService);
        .and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
	}
	
}
