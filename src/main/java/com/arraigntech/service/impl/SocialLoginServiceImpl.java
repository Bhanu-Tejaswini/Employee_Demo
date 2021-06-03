package com.arraigntech.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Service;

import com.arraigntech.entity.UserEntity;
import com.arraigntech.request.vo.SocialLoginVO;
import com.arraigntech.request.vo.UserVO;
import com.arraigntech.response.vo.LoginResponseVO;
import com.arraigntech.service.SocialLoginService;
import com.arraigntech.service.UserService;
import com.arraigntech.utility.AuthenticationProvider;
import com.arraigntech.utility.RandomPasswordGenerator;

@Service
public class SocialLoginServiceImpl implements SocialLoginService {

	public static final String ROLE_USER = "ROLE_USER";

	@Autowired
	private DefaultTokenServices tokenService;

	@Autowired
	private UserService userService;

	@Value("${client-id}")
	private String clientId;

	@Autowired
	private UserDetailServiceImpl userDetailsService;

	public LoginResponseVO getGoogleToken(SocialLoginVO socialLogin) {
		String username = getUsername(socialLogin.getUsername());
		UserEntity checkUser = userService.findByEmailAll(socialLogin.getEmail());
		if (Objects.isNull(checkUser)) {
			userService.register(new UserVO(username.toLowerCase(), socialLogin.getEmail(),
					RandomPasswordGenerator.generatePassword(), Arrays.asList(ROLE_USER),
					AuthenticationProvider.GOOGLE));
			UserEntity user = userService.findByEmailAll(socialLogin.getEmail());
			user.setActive(true);
			user.setEmailVerified(true);
			userService.saveUser(user);
			OAuth2AccessToken token = getAccessToken(user);
			return new LoginResponseVO(token.toString(), true);

		}
		OAuth2AccessToken token = getAccessToken(checkUser);
		return new LoginResponseVO(token.toString(), true);
	}

	public LoginResponseVO getFacebookToken(SocialLoginVO socialLogin) {
		String username = getUsername(socialLogin.getUsername());
		UserEntity checkUser = userService.findByEmailAll(socialLogin.getEmail());
		if (Objects.isNull(checkUser)) {
			userService.register(new UserVO(username.toLowerCase(), socialLogin.getEmail(),
					RandomPasswordGenerator.generatePassword(), Arrays.asList(ROLE_USER),
					AuthenticationProvider.FACEBOOK));
			UserEntity user = userService.findByEmailAll(socialLogin.getEmail());
			user.setActive(true);
			user.setEmailVerified(true);
			userService.saveUser(user);
			OAuth2AccessToken token = getAccessToken(user);
			return new LoginResponseVO(token.toString(), true);
		}
		OAuth2AccessToken token = getAccessToken(checkUser);
		return new LoginResponseVO(token.toString(), true);
	}

	private String getUsername(String value) {
		String[] newUsername = value.split(" ");
		String username = "";
		if (newUsername.length > 1) {
			for (int i = 0; i < newUsername.length; i++) {
				username = username + newUsername[i];
				if (i == newUsername.length - 1)
					break;
				username = username + "_";
			}
		} else {
			username = newUsername[0];
		}
		return username;
	}

	public OAuth2AccessToken getAccessToken(UserEntity user) {
		HashMap<String, String> authorizationParameters = new HashMap<String, String>();
		authorizationParameters.put("scope", "read");
		authorizationParameters.put("username", user.getEmail());
		authorizationParameters.put("client_id", clientId);
		authorizationParameters.put("grant", "password");

		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		user.getRoles().forEach((role) -> {
			role.getPermissions().forEach(permission -> {
				authorities.add(new SimpleGrantedAuthority(permission.getName()));
			});
		});

		Set<String> responseType = new HashSet<String>();
		responseType.add("password");

		Set<String> scopes = new HashSet<String>();
		scopes.add("read");
		scopes.add("write");

		OAuth2Request authorizationRequest = new OAuth2Request(authorizationParameters, clientId, authorities, true,
				scopes, null, "", responseType, null);

		UserEntity userPrincipal = (UserEntity) userDetailsService.loadUserByUsername(user.getEmail());
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal,
				null, authorities);

		OAuth2Authentication authenticationRequest = new OAuth2Authentication(authorizationRequest,
				authenticationToken);
		authenticationRequest.setAuthenticated(true);
		OAuth2AccessToken accessToken = tokenService.createAccessToken(authenticationRequest);
		return accessToken;
	}

}
