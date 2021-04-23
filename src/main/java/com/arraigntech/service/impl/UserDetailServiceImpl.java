package com.arraigntech.service.impl;

import java.util.Optional;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.arraigntech.Exception.AppException;
import com.arraigntech.entity.User;
import com.arraigntech.model.AuthUserDetail;
import com.arraigntech.repository.UserRespository;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

	public static final Logger log = LoggerFactory.getLogger(UserDetailServiceImpl.class);
	
	@Autowired
	private UserRespository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) {
		log.debug("loadUserByUsername method start");

		User newUser = userRepository.findByEmail(email);

		if(Objects.isNull(newUser)) {
			throw new AppException("Username or password is Invalid");
		}
		UserDetails userDetails = new AuthUserDetail(newUser);
		// checks account is valid or expired
		new AccountStatusUserDetailsChecker().check(userDetails);
		log.debug("loadUserByUsername method end");
		return userDetails;
	}		    
//
//	@Autowired
//	private UserRespository userRepository;
//
//	@Override
//	public UserDetails loadUserByUsername(String email) {
//		log.debug("loadUserByUsername method start");
//		User newUser = userRepository.findByEmail(email);
//
//		if (Objects.isNull(newUser)) {
//			throw new AppException("Username or password is Invalid");
//		}
//		AuthUserDetail userDetails = new AuthUserDetail();
//
//		Set<GrantedAuthority> grantedAuthority = new HashSet<>();
//		newUser.getRoles().forEach(role -> {
////			grantedAuthority.add(new SimpleGrantedAuthority(role.getName()));
//			role.getPermissions().forEach(permission -> {
//				grantedAuthority.add(new SimpleGrantedAuthority(permission.getName()));
//			});
//		});
//		// checks account is valid or expired
////		new AccountStatusUserDetailsChecker().check(userDetails);
////		log.debug("loadUserByUsername method end");
//		userDetails.setUser(newUser);
//		userDetails.setAuthorities(grantedAuthority);
//
//		return userDetails;
//	}
}
