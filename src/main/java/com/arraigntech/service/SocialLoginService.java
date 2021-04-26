package com.arraigntech.service;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.arraigntech.entity.User;
import com.arraigntech.model.SocialLoginDTO;

public interface SocialLoginService {
	public String getGoogleToken(SocialLoginDTO socialLogin);
	public String getFacebookToken(SocialLoginDTO socialLogin);
	public OAuth2AccessToken getAccessToken(User user);

}
