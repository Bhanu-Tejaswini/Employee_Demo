package com.arraigntech.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.arraigntech.Exception.AppException;
import com.arraigntech.entity.User;
import com.arraigntech.model.AuthUserDetail;
import com.arraigntech.repository.UserRespository;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserRespository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> optionalUser = userRepository.findByUsername(username);
		optionalUser.orElseThrow(() -> new AppException("Username or password is Invalid"));

//		User user=optionalUser.get();

		UserDetails userDetails = new AuthUserDetail(optionalUser.get());
		// checks account is valid or expired
		new AccountStatusUserDetailsChecker().check(userDetails);
		return userDetails;
	}

}
