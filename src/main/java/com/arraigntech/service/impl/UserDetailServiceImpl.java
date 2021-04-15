package com.arraigntech.service.impl;

import java.util.Optional;

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
	public UserDetails loadUserByUsername(String username) {
		log.debug("loadUserByUsername method start");

		Optional<User> optionalUser = userRepository.findByUsername(username);
		optionalUser.orElseThrow(() -> new AppException("Username or password is Invalid"));

//		User user=optionalUser.get();

		UserDetails userDetails = new AuthUserDetail(optionalUser.get());
		// checks account is valid or expired
		new AccountStatusUserDetailsChecker().check(userDetails);
		log.debug("loadUserByUsername method end");
		return userDetails;
	}

}
