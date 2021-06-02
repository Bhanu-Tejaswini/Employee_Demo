package com.arraigntech.service;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.arraigntech.entity.UserEntity;
import com.arraigntech.request.SocialLoginVO;
import com.arraigntech.response.LoginResponseVO;

public interface SocialLoginService {
	public LoginResponseVO getGoogleToken(SocialLoginVO socialLogin);
	public LoginResponseVO getFacebookToken(SocialLoginVO socialLogin);
	public OAuth2AccessToken getAccessToken(UserEntity user);

}
