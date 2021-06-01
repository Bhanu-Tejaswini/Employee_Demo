/**
 * 
 */
package com.arraigntech.service;

import com.arraigntech.entity.EmailSettingEntity;
import com.arraigntech.entity.UserEntity;
import com.arraigntech.exceptions.AppException;
import com.arraigntech.request.vo.EmailSettingsVO;
import com.arraigntech.request.vo.LoginDetailsVO;
import com.arraigntech.request.vo.UserVO;
import com.arraigntech.response.vo.LoginResponseVO;

/**
 * @author Bhaskara S
 *
 */
public interface UserService {
	
	public Boolean register(UserVO userDTO);
	public UserEntity findByEmailAll(String email);
	public void saveUser(UserEntity newUser);
	public UserEntity findByEmail(String email);
	public UserEntity findByUsernameAndIdNot(String username, String id);
	public UserEntity findByEmailAndIdNot(String email,String id);
	public boolean delete(String password) throws AppException;
	public String updatePassword(String token, String newPassword);
	public LoginResponseVO generateToken(LoginDetailsVO login);
	public Boolean forgotPassword(String email);
	public String updateAccountPassword(String oldPassword, String newPassword);
	public EmailSettingsVO getEmailSetting();
	public String saveEmailSettings(EmailSettingsVO emailSettings);
	public boolean persistEmailSettings(EmailSettingEntity settings, EmailSettingsVO emailSettings, UserEntity user);
	public Boolean sendRegisterationLink(String userEmail);
	public Boolean verifyRegisterationToken(String token);
	public Boolean welcomeMail(String userEmail, String password);
	public UserEntity getUser();
	public boolean getWelcomeMailTemplateDetails(String userEmail);
}
