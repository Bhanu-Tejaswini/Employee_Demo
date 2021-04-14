package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.demo.entity.User;
import com.example.demo.model.AuthUserDetail;
import com.example.demo.repository.UserRespository;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserRespository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> optionalUser = userRepository.findByUsername(username);
		optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username or password wrong"));

//		User user=optionalUser.get();

		UserDetails userDetails = new AuthUserDetail(optionalUser.get());
		// checks account is valid or expired
		new AccountStatusUserDetailsChecker().check(userDetails);
		return userDetails;
	}

}
