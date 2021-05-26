package com.arraigntech.service;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.arraigntech.entity.User;
import com.arraigntech.model.LoginResponseDTO;
import com.arraigntech.model.SocialLoginDTO;

public interface SocialLoginService {
	public LoginResponseDTO getGoogleToken(SocialLoginDTO socialLogin);
	public LoginResponseDTO getFacebookToken(SocialLoginDTO socialLogin);
	public OAuth2AccessToken getAccessToken(User user);

}
