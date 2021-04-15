package com.example.demo.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.example.demo.entity.User;

public class CustomTokenEnhancer implements TokenEnhancer {
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		Map<String, Object> additionalInfo = new LinkedHashMap<>(accessToken.getAdditionalInformation());
		additionalInfo.put("email", user.getEmail());
		additionalInfo.put("resourceids",authentication.getOAuth2Request().getResourceIds());
//		additionalInfo.put("roles", user.getRoles());
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		return accessToken;
	}

}
