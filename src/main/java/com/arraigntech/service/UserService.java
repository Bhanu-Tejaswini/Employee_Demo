/**
 * 
 */
package com.arraigntech.service;

import com.arraigntech.entity.EmailSettings;
import com.arraigntech.entity.User;
import com.arraigntech.exceptions.AppException;
import com.arraigntech.model.EmailSettingsModel;
import com.arraigntech.model.LoginDetails;
import com.arraigntech.model.LoginResponseDTO;
import com.arraigntech.model.UserDTO;

/**
 * @author Bhaskara S
 *
 */
public interface UserService {
	
	public Boolean register(UserDTO userDTO);
	public User findByEmailAll(String email);
	public void saveUser(User newUser);
	public User findByEmail(String email);
	public User findByUsernameAndIdNot(String username, String id);
	public User findByEmailAndIdNot(String email,String id);
	public boolean delete(String password) throws AppException;
	public String updatePassword(String token, String newPassword);
	public LoginResponseDTO generateToken(LoginDetails login);
	public Boolean forgotPassword(String email);
	public String updateAccountPassword(String oldPassword, String newPassword);
	public EmailSettingsModel getEmailSetting();
	public String saveEmailSettings(EmailSettingsModel emailSettings);
	public boolean persistEmailSettings(EmailSettings settings, EmailSettingsModel emailSettings, User user);
	public Boolean sendRegisterationLink(String userEmail);
	public Boolean verifyRegisterationToken(String token);
	public Boolean welcomeMail(String userEmail, String password);
	public User getUser();
	public boolean getWelcomeMailTemplateDetails(String userEmail);
}
