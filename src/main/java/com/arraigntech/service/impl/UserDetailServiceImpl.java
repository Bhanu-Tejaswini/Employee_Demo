package com.arraigntech.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.arraigntech.entity.UserEntity;
import com.arraigntech.exceptions.AppException;
import com.arraigntech.repository.UserRespository;
import com.arraigntech.request.AuthUserDetailVO;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserRespository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) {
		UserEntity newUser = userRepo.findByEmailAll(email);

		if (Objects.isNull(newUser)) {
			throw new AppException("Username or password is Invalid");
		}
		UserDetails userDetails = new AuthUserDetailVO(newUser);
		// checks account is valid or expired
		new AccountStatusUserDetailsChecker().check(userDetails);
		return userDetails;

	}
}
