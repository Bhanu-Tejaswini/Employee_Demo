/**
 * 
 */
package com.employee.service;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.employee.entity.EmployeeEntity;
import com.employee.request.vo.EmployeeVO;
import com.employee.request.vo.LoginDetailsVO;
import com.employee.response.vo.LoginResponseVO;

public interface EmployeeService {
	
	public Boolean register(EmployeeVO userDTO);
	public EmployeeEntity findByEmailAll(String email);
	public void saveUser(EmployeeEntity newUser);
	public EmployeeEntity findByEmail(String email);
	public EmployeeEntity findByUsernameAndIdNot(String username, String id);
	public EmployeeEntity findByEmailAndIdNot(String email,String id);
	public LoginResponseVO generateToken(LoginDetailsVO login);
	public Boolean sendRegisterationLink(String userEmail);
	public Boolean verifyRegisterationToken(String token);
	public EmployeeEntity getUser();
	public OAuth2AccessToken getAccessToken(EmployeeEntity user);

}
