
package com.arraigntech.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.arraigntech.Exception.AppException;
import com.arraigntech.entity.EmailSettings;
import com.arraigntech.entity.ResetToken;
import com.arraigntech.entity.Role;
import com.arraigntech.entity.User;
import com.arraigntech.model.Email;
import com.arraigntech.model.EmailSettingsModel;
import com.arraigntech.model.LoginDetails;
import com.arraigntech.model.TokenResponse;
import com.arraigntech.model.UserDTO;
import com.arraigntech.model.UserToken;
import com.arraigntech.repository.EmailSettingsRepository;
import com.arraigntech.repository.ResetTokenRepository;
import com.arraigntech.repository.RoleRepository;
import com.arraigntech.repository.UserRespository;
import com.arraigntech.service.IVSService;
import com.arraigntech.service.MailService;
import com.arraigntech.utility.EmailValidator;
import com.arraigntech.utility.FormEmailData;
import com.arraigntech.utility.IVSJwtUtil;
import com.arraigntech.utility.MessageConstants;
import com.arraigntech.utility.PasswordConstraintValidator;

@Service
public class UserServiceImpl implements IVSService<User, String> {
	
	public static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRespository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${basic.auth}")
	private String basicAuth;

	@Autowired
	private IVSJwtUtil jwtUtil;

	@Autowired
	private MailService mailService;

	@Autowired
	private EmailSettingsRepository emailSettingsRepo;

	@Autowired
	private EmailValidator emailValidator;

	@Autowired
	private PasswordConstraintValidator passwordValidator;

	@Value("${reset-password-baseUrl}")
	private String baseUrl;
	
	@Value("${reset-password-scheme}")
	private String scheme;
	
	@Autowired
	private ResetTokenRepository resetTokenRepo;
	
	@Autowired
	private FormEmailData formEmailData;
	
	@Value("${registrationlink-baseUrl}")
	private String registrationBaseurl;
	
	@Value("${spring.mail.username}")
	private String formMail;

	public Boolean register(UserDTO userDTO) throws AppException {
		System.out.println(emailValidator.isValidEmail(userDTO.getEmail()));
		if (userDTO.getEmail() == null || !emailValidator.isValidEmail(userDTO.getEmail())) {
			throw new AppException(MessageConstants.INVALID_EMAIL);
		}
		User newUser = userRepo.findByEmail(userDTO.getEmail());

		if (Objects.nonNull(newUser)) {
			throw new AppException(MessageConstants.USER_EXISTS);
		}
		Optional<User> newUser1 = userRepo.findByUsername(userDTO.getUsername());
		if (newUser1.isPresent()) {
			throw new AppException(MessageConstants.USER_EXISTS_USERNAME);
		}
		if (!passwordValidator.isValid(userDTO.getPassword())) {
			throw new AppException(MessageConstants.INVALID_PASSWORD);
		}
		newUser = new User();
		newUser.setUsername(userDTO.getUsername());
		newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		newUser.setEmail(userDTO.getEmail());
		newUser.setEnabled(true);
		newUser.setAccountNonExpired(true);
		newUser.setAccountNonLocked(true);
		newUser.setCredentialsNonExpired(true);
		newUser.setActive(true);

		for (String str : userDTO.getRole()) {
			Role role = roleRepo.findByName(str);
			newUser.getRoles().add(role);
		}
		userRepo.save(newUser);

		return true;
	}

	@Override
	public Page<User> getPaginated(Integer page, Integer limit) {
		return userRepo.findAll(PageRequest.of(page, limit));
	}

	@Override
	public List<User> getAll() throws AppException {
		List<User> users = userRepo.findAll();
		if (users.isEmpty()) {
			throw new AppException(MessageConstants.USER_LIST_EMPTY);
		}
		return users;
	}

	@Override
	public User update(User entity) throws AppException {
		User newUser = new User();
		newUser = userRepo.findByEmail(entity.getEmail());
		if (Objects.isNull(newUser)) {
			throw new AppException("The user already exists");
		}
		return userRepo.save(entity);
	}


	public boolean delete(String password) throws AppException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> optionalUser = userRepo.findByUsername(authentication.getName());
		if (!optionalUser.isPresent()) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		if (passwordEncoder.matches(password, optionalUser.get().getPassword())) {
			optionalUser.get().setActive(false);
		} else {
			throw new AppException(MessageConstants.WRONG_PASSWORD);
		}
		userRepo.save(optionalUser.get());
		return true;
	}

	@Override
	public User create(User entity) {
		// TODO Auto-generated method stub
		return null;
	}

	public String updatePassword(String token, String newPassword) throws AppException {

		if (token.isEmpty() || newPassword.isEmpty()) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if (!passwordValidator.isValid(newPassword)) {
			throw new AppException(MessageConstants.INVALID_PASSWORD);
		}
		if (!(StringUtils.hasText(token) && jwtUtil.validateToken(token))) {
			throw new AppException(MessageConstants.TOKEN_NOT_FOUND);
		}
		String email = jwtUtil.getUsernameFromToken(token);
		Date expiryDate = jwtUtil.getExpirationTimeFromToken(token);
		System.out.println("email" + email + "  expiry date" + expiryDate);
		User newUser = userRepo.findByEmail(email);
		if (Objects.isNull(newUser)) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		//check whether token exist in the database or not
		ResetToken resetToken=resetTokenRepo.findByUser(newUser);
		if(Objects.isNull(resetToken)) {
			throw new AppException(MessageConstants.TOKEN_EXPIRED);
		}
		newUser.setPassword(passwordEncoder.encode(newPassword));
		userRepo.save(newUser);
		//deleting the token after reseting the password
		resetTokenRepo.deleteById(resetToken.getId());
		return MessageConstants.PASSWORDMESSAGE;
	}

	public String generateToken(LoginDetails login, UriComponentsBuilder builder) throws AppException {
		if (login.getEmail().isEmpty() || login.getPassword().isEmpty()) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if (!emailValidator.isValidEmail(login.getEmail())) {
			throw new AppException(MessageConstants.INVALID_EMAIL);
		}
		User newUser = userRepo.findByEmail(login.getEmail());
		if (Objects.isNull(newUser)) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		if (!passwordEncoder.matches(login.getPassword(), newUser.getPassword())) {
			throw new AppException(MessageConstants.WRONG_PASSWORD);
		}

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Authorization", basicAuth);

		String localUrl = builder.path("/oauth/token").build().toUriString();

		String uri = localUrl + "?grant_type=password&username=" + login.getEmail() + "&password="
				+ login.getPassword();
		HttpEntity<UserToken> request = new HttpEntity<>(headers);
		ResponseEntity<TokenResponse> reponseEntity = restTemplate.exchange(uri, HttpMethod.POST, request,
				TokenResponse.class);
		TokenResponse response = reponseEntity.getBody();
		return response.getAccess_token();
	}

	@Override
	public User getById(String id) throws AppException {
		Optional<User> newUser = userRepo.findById(id);
		if (!newUser.isPresent()) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		return newUser.get();
	}

	public Boolean forgotPassword(String email) {
		if (email == null || !emailValidator.isValidEmail(email)) {
			throw new AppException(MessageConstants.INVALID_EMAIL);
		}
		User newUser = userRepo.findByEmail(email);
		if (Objects.isNull(newUser)) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		//generating the token
		String token = jwtUtil.generateResetToken(email);
		//saving the resettoken in the database
		ResetToken resetToken=new ResetToken(token,newUser);
		resetTokenRepo.save(resetToken);
		// Generating the password reset link
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		String passwordResetLink = builder.scheme(scheme).host(baseUrl).path("/auth/setPassword").queryParam("token", token)
				.buildAndExpand(token).toUriString();
		// Sending the mail with password reset link
		try {
			mailService.sendEmail(email, passwordResetLink);
		} catch (UnsupportedEncodingException | MessagingException e) {
			throw new AppException(e.getMessage());
		}
		return true;
	}

	public String updateAccountPassword(String oldPassword, String newPassword) {
		if (oldPassword.isEmpty() || newPassword.isEmpty()) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if (!passwordValidator.isValid(newPassword)) {
			throw new AppException(MessageConstants.INVALID_PASSWORD);
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> optionalUser = userRepo.findByUsername(authentication.getName());
		if (!optionalUser.isPresent()) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		User newUser = optionalUser.get();
		if (passwordEncoder.matches(oldPassword, newUser.getPassword())) {
			newUser.setPassword(passwordEncoder.encode(newPassword));
		} else {
			throw new AppException(MessageConstants.WRONG_PASSWORD);
		}
		userRepo.save(newUser);
		return MessageConstants.PASSWORDMESSAGE;
	}

	public EmailSettingsModel getEmailSetting() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> optionalUser = userRepo.findByUsername(authentication.getName());

		if (!optionalUser.isPresent()) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		EmailSettings emailSettings = emailSettingsRepo.findByUser(optionalUser.get());
		if (Objects.isNull(emailSettings)) {
			return new EmailSettingsModel(true, true, true, true, true, true);
		}
		return new EmailSettingsModel(emailSettings.isSystemAlerts(), emailSettings.isMonthlyStreamingReports(),
				emailSettings.isInstantStreamingReport(), emailSettings.isPromotions(),
				emailSettings.isProductUpdates(), emailSettings.isBlogDigest());
	}

	public String saveEmailSettings(EmailSettingsModel emailSettings) {
		if (Objects.isNull(emailSettings)) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> optionalUser = userRepo.findByUsername(authentication.getName());
		if (!optionalUser.isPresent()) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		EmailSettings checkSettings = emailSettingsRepo.findByUser(optionalUser.get());
		if (Objects.isNull(checkSettings)) {
			EmailSettings newSettings = new EmailSettings();
			persistEmailSettings(newSettings, emailSettings, optionalUser.get());
		} else {
			persistEmailSettings(checkSettings, emailSettings, optionalUser.get());
		}

		return MessageConstants.EMAILSETTINGSMESSAGE;
	}

	public boolean persistEmailSettings(EmailSettings settings, EmailSettingsModel emailSettings, User user) {
		settings.setUser(user);
		settings.setSystemAlerts(emailSettings.getSystemAlerts());
		settings.setMonthlyStreamingReports(emailSettings.getMonthlyStreamingReports());
		settings.setInstantStreamingReport(emailSettings.getInstantStreamingReport());
		settings.setPromotions(emailSettings.getPromotions());
		settings.setProductUpdates(emailSettings.getProductUpdates());
		settings.setBlogDigest(emailSettings.getBlogDigest());
		emailSettingsRepo.save(settings);
		return true;
	}

	public Boolean sendRegisterationLink(UserDTO user) {
		log.debug("sendRegisterationLink method start");
		try {
			if (user.getEmail() == null || !emailValidator.isValidEmail(user.getEmail())) {
				throw new AppException(MessageConstants.INVALID_EMAIL);
			}
			String token = jwtUtil.generateResetToken(user.getEmail());
			Map<String, String> model = new HashMap<String, String>();
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
			String regisrationLink = builder.scheme(scheme).host(registrationBaseurl).path("/auth/register").queryParam("token", token)
					.buildAndExpand(token).toUriString();
			model.put(MessageConstants.REGISTRATION_CONFIRMATION_TOKEN, regisrationLink);
			Email email = formEmailData.formEmail(formMail, user.getEmail(),
					MessageConstants.REGISTRATION_CONFIRMATION_LINK, model);
			mailService.sendEmail(email);
			log.debug("sendRegisterationLink method end");
		} catch (Exception e) {
			log.error("Error in sending activate notification email to vendor : " + user.getEmail(), e);
			throw new AppException("Something went wrong, Please try again later.");
		}

		return true;
	}
	
//	public String saveUserSettings(UserSettingsDTO userSettings) {
////		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////		Optional<User> optionalUser=userRepo.findByUsername(authentication.getName());
//		Optional<User> optionalUser=userRepo.findByUsername("user1");
//		if(!optionalUser.isPresent()) {
//			throw new AppException(MessageConstants.USER_NOT_FOUND);
//		}
//		User newUser=optionalUser.get();
//		newUser.setEmail(userSettings.getEmail());
//		newUser.setMobileNumber(userSettings.getMobilenumber());
//		newUser.setUsername(userSettings.getUsername());
//		newUser.setPincode(userSettings.getPincode());
//		newUser.setLanguage(userSettings.getLanguage());
//		newUser.setTimeZone(userSettings.getTimezone());
//		userRepo.save(newUser);
//		return MessageConstants.USER_SETTINGS_UPDATED;
//	}
	
}
