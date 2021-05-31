package com.arraigntech.service.impl;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.arraigntech.entity.User;
import com.arraigntech.exceptions.AppException;
import com.arraigntech.model.AuthUserDetail;
import com.arraigntech.repository.UserRespository;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

	public static final Logger log = LoggerFactory.getLogger(UserDetailServiceImpl.class);

	@Autowired
	private UserRespository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) {
		log.debug("loadUserByUsername method start");

		User newUser = userRepo.findByEmailAll(email);

		if (Objects.isNull(newUser)) {
			throw new AppException("Username or password is Invalid");
		}
		UserDetails userDetails = new AuthUserDetail(newUser);
		// checks account is valid or expired
		new AccountStatusUserDetailsChecker().check(userDetails);
		log.debug("loadUserByUsername method end");
		return userDetails;

	}
}
