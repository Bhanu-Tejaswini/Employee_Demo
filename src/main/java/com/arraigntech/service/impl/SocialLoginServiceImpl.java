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

import com.arraigntech.entity.Role;
import com.arraigntech.entity.User;
import com.arraigntech.model.SocialLoginDTO;
import com.arraigntech.model.UserDTO;
import com.arraigntech.repository.RoleRepository;
import com.arraigntech.repository.UserRespository;
import com.arraigntech.utility.AuthenticationProvider;

@Service
public class SocialLoginServiceImpl {

	@Autowired
	private DefaultTokenServices tokenService;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private UserRespository userRepo;
	
	@Autowired
	private UserServiceImpl userService;

	@Value("${client-id}")
	private String clientId;

	@Autowired
	private UserDetailServiceImpl userDetailsService;
	
	public String getToken(SocialLoginDTO socialLogin) {
		//checks whether email is verified or not
		boolean emailVerified=socialLogin.getAuthorities().get(0).getAttributes().isEmail_verified();
		String getEmail=socialLogin.getAuthorities().get(0).getAttributes().getEmail();
		String getUsername=socialLogin.getAuthorities().get(0).getAttributes().getName();
		String role=socialLogin.getAuthorities().get(0).getAuthority();
		User checkUser=userRepo.findByEmail(getEmail);
		if(Objects.isNull(checkUser)) {
			userService.register(new UserDTO(getUsername,getEmail,"Google123",Arrays.asList(role),AuthenticationProvider.GOOGLE));
		}
		User newUser = new User();
		newUser.setUsername(getUsername);
		newUser.setEmail(getEmail);
		newUser.setPassword("");
		Role newRole = roleRepo.findByName(role);
		newUser.getRoles().add(newRole);
		OAuth2AccessToken token=getAccessToken(newUser);
		return token.toString();
	}
	

	public OAuth2AccessToken getAccessToken(User user) {
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

		User userPrincipal =  (User) userDetailsService.loadUserByUsername(user.getEmail());
//				new org.springframework.security.core.userdetails.User(
//				user.getEmail(), user.getPassword(), authorities);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal,
				null, authorities);

		OAuth2Authentication authenticationRequest = new OAuth2Authentication(authorizationRequest,
				authenticationToken);
		authenticationRequest.setAuthenticated(true);
		OAuth2AccessToken accessToken = tokenService.createAccessToken(authenticationRequest);

		return accessToken;
	}



}
