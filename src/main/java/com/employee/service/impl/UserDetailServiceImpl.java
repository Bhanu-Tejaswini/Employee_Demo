package com.employee.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.employee.entity.EmployeeEntity;
import com.employee.exceptions.AppException;
import com.employee.repository.EmployeeRespository;
import com.employee.request.vo.AuthUserDetailVO;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private EmployeeRespository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) {
		EmployeeEntity newUser = userRepo.findByEmailAll(email);

		if (Objects.isNull(newUser)) {
			throw new AppException("Username or password is Invalid");
		}
		UserDetails userDetails = new AuthUserDetailVO(newUser);
		// checks account is valid or expired
		new AccountStatusUserDetailsChecker().check(userDetails);
		return userDetails;

	}
}
