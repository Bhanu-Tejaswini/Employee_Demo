package com.arraigntech.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.arraigntech.entity.EmailSettings;
import com.arraigntech.entity.ResetToken;
import com.arraigntech.entity.Role;
import com.arraigntech.entity.User;
import com.arraigntech.exceptions.AppException;
import com.arraigntech.model.Email;
import com.arraigntech.model.EmailSettingsModel;
import com.arraigntech.model.LoginDetails;
import com.arraigntech.model.LoginResponseDTO;
import com.arraigntech.model.UserDTO;
import com.arraigntech.repository.EmailSettingsRepository;
import com.arraigntech.repository.ResetTokenRepository;
import com.arraigntech.repository.RoleRepository;
import com.arraigntech.repository.UserRespository;
import com.arraigntech.service.MailService;
import com.arraigntech.service.UserService;
import com.arraigntech.utility.CommonUtils;
import com.arraigntech.utility.EmailValidator;
import com.arraigntech.utility.FormEmailData;
import com.arraigntech.utility.IVSJwtUtil;
import com.arraigntech.utility.MessageConstants;
import com.arraigntech.utility.PasswordConstraintValidator;

import freemarker.template.TemplateException;

@Service
public class UserServiceImpl implements UserService {

	public static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRespository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepo;

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

	@Value("${app.scheme}")
	private String appScheme;

	@Value("${app.schemeHost}")
	private String appSchemeHost;

	@Autowired
	private ResetTokenRepository resetTokenRepo;

	@Autowired
	private SocialLoginServiceImpl socialLoginService;

	@Autowired
	private FormEmailData formEmailData;

	@Value("${registrationlink-baseUrl}")
	private String registrationBaseurl;

	@Value("${spring.mail.username}")
	private String formMail;

	@Value("${jwt.resetTokenExpirationTime}")
	private int resetTokenExpirationTime;

	@Value("${jwt.verification.mail.expiryTime}")
	private int verficationMailExpirationTime;

	@Autowired
	private IVSJwtUtil iVSJwtUtil;

	public static final String ROLE_USER = "ROLE_USER";
	public static final String vstreemImage = "https://vstreem-images.s3.us-east-2.amazonaws.com/email-template-images/vestreem_logo.png";
	public static final String welcomeImage = "https://vstreem-images.s3.us-east-2.amazonaws.com/email-template-images/welcome.jpg";
	public static final String galleryImage = "https://vstreem-images.s3.us-east-2.amazonaws.com/email-template-images/gallery.png";
	public static final String databondImage = "https://vstreem-images.s3.us-east-2.amazonaws.com/email-template-images/databond.png";
	public static final String combistreemImage = "https://vstreem-images.s3.us-east-2.amazonaws.com/email-template-images/combistreem.png";
	public static final String catalogueImage = "https://vstreem-images.s3.us-east-2.amazonaws.com/email-template-images/catalogue.png";

	@Override
	public Boolean register(UserDTO userDTO) {
		log.debug("register start");
		if (Objects.isNull(userDTO) || !StringUtils.hasText(userDTO.getUsername())
				|| !StringUtils.hasText(userDTO.getPassword())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if (userDTO.getEmail() == null || !emailValidator.isValidEmail(userDTO.getEmail())) {
			throw new AppException(MessageConstants.INVALID_EMAIL);
		}
		User newUser = userRepo.findByEmailAll(userDTO.getEmail());

		if (Objects.nonNull(newUser)) {
			throw new AppException(MessageConstants.USER_EXISTS);
		}
		if (!passwordValidator.isValid(userDTO.getPassword())) {
			throw new AppException(MessageConstants.INVALID_PASSWORD);
		}

		newUser = new User();
		newUser.setProvider(userDTO.getProvider());
		newUser.setUsername(userDTO.getUsername());
		newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		newUser.setEmail(userDTO.getEmail());
		newUser.setAccountNonExpired(true);
		newUser.setAccountNonLocked(true);
		newUser.setCredentialsNonExpired(true);
		newUser.setActive(false);
		newUser.setEmailVerified(false);
		Role role = roleRepo.findByName(ROLE_USER);
		newUser.getRoles().add(role);
		newUser.setEnabled(true);
		userRepo.save(newUser);
		if (Objects.isNull(newUser.getProvider())) {
			sendRegisterationLink(userDTO.getEmail());
		} else {
			welcomeMail(userDTO.getEmail(), userDTO.getPassword());
		}
		log.debug("register end");
		return true;
	}

	@Override
	public User findByEmailAll(String email) {
		return userRepo.findByEmailAll(email);
	}

	@Override
	public void saveUser(User newUser) {
		userRepo.save(newUser);
	}

	@Override
	public User findByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Override
	public User findByUsernameAndIdNot(String username, String id) {
		return userRepo.findByUsernameAndIdNot(username, id);
	}

	@Override
	public User findByEmailAndIdNot(String email, String id) {
		return userRepo.findByEmailAndIdNot(email, id);
	}

	@Override
	public boolean delete(String password) {
		User newUser = getUser();
		if (passwordEncoder.matches(password, newUser.getPassword())) {
			newUser.setActive(false);
		} else {
			throw new AppException(MessageConstants.WRONG_PASSWORD);
		}
		userRepo.save(newUser);
		return true;
	}

	@Override
	public String updatePassword(String token, String newPassword) {
		log.debug("reset password start");
		if (!StringUtils.hasText(token) || !StringUtils.hasText(newPassword)) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if (!passwordValidator.isValid(newPassword)) {
			throw new AppException(MessageConstants.INVALID_PASSWORD);
		}
		if (!(StringUtils.hasText(token) && jwtUtil.validateToken(token))) {
			throw new AppException(MessageConstants.TOKEN_NOT_FOUND);
		}
		String email = jwtUtil.getUsernameFromToken(token);

		User newUser = userRepo.findByEmail(email);
		if (Objects.isNull(newUser)) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		// check whether token exist in the database or not
		ResetToken resetToken = resetTokenRepo.findByUser(newUser);
		if (Objects.isNull(resetToken)) {
			throw new AppException(MessageConstants.TOKEN_EXPIRED);
		}
		newUser.setPassword(passwordEncoder.encode(newPassword));
		userRepo.save(newUser);
		// deleting the token after reseting the password
		resetTokenRepo.deleteById(resetToken.getId());
		log.debug(email);
		return MessageConstants.PASSWORDMESSAGE;
	}

	@Override
	public LoginResponseDTO generateToken(LoginDetails login) {
		log.debug("generateToken start");
		if (!StringUtils.hasText(login.getEmail()) || !StringUtils.hasText(login.getPassword())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if (!emailValidator.isValidEmail(login.getEmail())) {
			throw new AppException(MessageConstants.INVALID_EMAIL);
		}
		User newUser = userRepo.findByEmailAll(login.getEmail());
		if (Objects.isNull(newUser)) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		userRepo.save(newUser);
		if (!newUser.isEmailVerified()) {
			if (System.currentTimeMillis() < newUser.getUpdatedAt().getTime() + 900000) {
				return new LoginResponseDTO(MessageConstants.VERIFICATION_MAIL_ALREADYSENT, false);
			} else {
				sendRegisterationLink(login.getEmail());
				return new LoginResponseDTO(MessageConstants.TOKEN_EXPIRED_RESENDMAIL, false);
			}
		}
		if (!newUser.isActive()) {
			throw new AppException(MessageConstants.ACCOUNT_DISABLED);
		}
		if (!passwordEncoder.matches(login.getPassword(), newUser.getPassword())) {
			throw new AppException(MessageConstants.WRONG_PASSWORD);
		}
		OAuth2AccessToken accessToken = socialLoginService.getAccessToken(newUser);
		log.debug("generateToken end");
		return new LoginResponseDTO(accessToken.toString(), true);

	}

	@Override
	public Boolean forgotPassword(String email) {
		log.debug("forgotPassword start");
		if (!StringUtils.hasText(email) || !emailValidator.isValidEmail(email)) {
			throw new AppException(MessageConstants.INVALID_EMAIL);
		}
		User newUser = userRepo.findByEmailAll(email);
		if (Objects.isNull(newUser)) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		if (!newUser.isActive()) {
			throw new AppException(MessageConstants.ACCOUNT_DISABLED);
		}
		// generating the token
		String token = jwtUtil.generateResetToken(email, resetTokenExpirationTime);
		// saving the resettoken in the database
		ResetToken resetToken = resetTokenRepo.findByUser(newUser);
		if (Objects.nonNull(resetToken)) {
			resetToken.setToken(token);
		} else {
			resetToken = new ResetToken(token, newUser);
		}
		resetTokenRepo.save(resetToken);
		// Generating the password reset link
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		String passwordResetLink = builder.scheme(scheme).host(baseUrl).path("/auth/setPassword")
				.queryParam("token", token).buildAndExpand(token).toUriString();
		Map model = new HashMap();
		model.put("userMail", email);
		model.put("regisrationLink", passwordResetLink);
		model.put("vstreemImage", vstreemImage);
		Email emailDetails = formEmailData.formEmail(formMail, email, MessageConstants.RESET_PASSWORD_LINK,
				passwordResetLink, "ResetPasswordTemplate.html", model);
		// Sending the mail with password reset link
		try {
			mailService.sendEmail(emailDetails);
		} catch (MessagingException | IOException | TemplateException e) {
			e.printStackTrace();
		}
		log.debug("forgotPassword end");
		return true;
	}

	@Override
	public String updateAccountPassword(String oldPassword, String newPassword) {
		log.debug("updateAccountPassword start");
		if (!StringUtils.hasText(oldPassword) || !StringUtils.hasText(newPassword)) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if (!passwordValidator.isValid(newPassword)) {
			throw new AppException(MessageConstants.INVALID_PASSWORD);
		}
		User newUser = getUser();
		if (passwordEncoder.matches(oldPassword, newUser.getPassword())) {
			newUser.setPassword(passwordEncoder.encode(newPassword));
		} else {
			throw new AppException(MessageConstants.WRONG_PASSWORD);
		}
		saveUser(newUser);
		log.debug("updateAccountPassword end");
		return MessageConstants.PASSWORDMESSAGE;
	}

	@Override
	public EmailSettingsModel getEmailSetting() {
		log.debug("getEmailSetting start");
		User newUser = getUser();
		EmailSettings emailSettings = emailSettingsRepo.findByUser(newUser);
		if (Objects.isNull(emailSettings)) {
			return new EmailSettingsModel(true, true, true, true, true, true);
		}
		log.debug("getEmailSettings end");
		return new EmailSettingsModel(emailSettings.isSystemAlerts(), emailSettings.isMonthlyStreamingReports(),
				emailSettings.isInstantStreamingReport(), emailSettings.isPromotions(),
				emailSettings.isProductUpdates(), emailSettings.isBlogDigest());
	}

	@Override
	public String saveEmailSettings(EmailSettingsModel emailSettings) {
		log.debug("saveEmailSettings start");
		if (Objects.isNull(emailSettings)) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		User newUser = getUser();
		EmailSettings checkSettings = emailSettingsRepo.findByUser(newUser);
		if (Objects.isNull(checkSettings)) {
			EmailSettings newSettings = new EmailSettings();
			persistEmailSettings(newSettings, emailSettings, newUser);
		} else {
			persistEmailSettings(checkSettings, emailSettings, newUser);
		}
		log.debug("saveEmailSettings end");
		return MessageConstants.EMAILSETTINGSMESSAGE;
	}

	@Override
	public boolean persistEmailSettings(EmailSettings settings, EmailSettingsModel emailSettings, User user) {
		log.debug("persistEmailSettings start");
		settings.setUser(user);
		settings.setSystemAlerts(emailSettings.getSystemAlerts());
		settings.setMonthlyStreamingReports(emailSettings.getMonthlyStreamingReports());
		settings.setInstantStreamingReport(emailSettings.getInstantStreamingReport());
		settings.setPromotions(emailSettings.getPromotions());
		settings.setProductUpdates(emailSettings.getProductUpdates());
		settings.setBlogDigest(emailSettings.getBlogDigest());
		emailSettingsRepo.save(settings);
		log.debug("persistEmailSettings end");
		return true;
	}

	@Override
	public Boolean sendRegisterationLink(String userEmail) {
		log.debug("sendRegisterationLink method start");
		try {
			if (!StringUtils.hasText(userEmail) || !emailValidator.isValidEmail(userEmail)) {
				throw new AppException(MessageConstants.INVALID_EMAIL);
			}
			String token = jwtUtil.generateResetToken(userEmail, verficationMailExpirationTime);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance();

			String regisrationLink = builder.scheme("https").host(registrationBaseurl).path("/auth")
					.queryParam("token", token).buildAndExpand(token).toUriString();
			Map model = new HashMap();
			model.put("userMail", userEmail);
			model.put("regisrationLink", regisrationLink);
			model.put("vstreemImage", vstreemImage);
			Email email = formEmailData.formEmail(formMail, userEmail, MessageConstants.REGISTRATION_CONFIRMATION_LINK,
					regisrationLink, "MailVerificationTemplate.html", model);
			User newUser = findByEmailAll(userEmail);
			newUser.setUpdatedAt(new Date());
			userRepo.save(newUser);
			mailService.sendEmail(email);
			log.debug("sendRegisterationLink method end");
		} catch (Exception e) {
			log.error("Error in sending registration link : " + userEmail, e);
			throw new AppException("Something went wrong, Please try again later.");
		}
		return true;
	}

	@Override
	public Boolean verifyRegisterationToken(String token) {
		log.debug("verifyRegisterationToken method start");
		if (!StringUtils.hasText(token)) {
			throw new AppException(MessageConstants.INVALID_REGISTER_TOKEN);
		}

		if (!iVSJwtUtil.validateRegisterToken(token)) {
			return false;
		}

		String email = iVSJwtUtil.getUsernameFromToken(token);
		User user = findByEmailAll(email);
		if (Objects.nonNull(user)) {
			user.setActive(true);
			user.setEmailVerified(true);
			userRepo.save(user);
			getWelcomeMailTemplateDetails(email);
			return true;
		}
		return false;
	}

	@Override
	public Boolean welcomeMail(String userEmail, String password) {
		log.debug("getWelcomeMailTemplateDetails method start");
		Map model = new HashMap();
		model.put("userMail", userEmail);
		model.put("vstreemImage", vstreemImage);
		model.put("welcomeImage", welcomeImage);
		model.put("galleryImage", galleryImage);
		model.put("combistreemImage", combistreemImage);
		model.put("databondImage", databondImage);
		model.put("catalogueImage", catalogueImage);
		model.put("password", password);
		Email email = formEmailData.formEmail(formMail, userEmail, MessageConstants.WELCOME_TEMPLATE_SUBJECT, null,
				"SocialWelcomeTemplate.html", model);
		try {
			mailService.sendEmail(email);
		} catch (Exception e) {
			throw new AppException("Something went wrong while sending mail, Please try again later.");
		}
		log.debug("getWelcomeMailTemplateDetails method end");

		return true;
	}

	public User getUser() {
		if(log.isDebugEnabled()) {
			 log.debug("getting the user from token");
		}
		User user = findByEmail(CommonUtils.getUser());
		if(log.isDebugEnabled()) {
			 log.debug("getting the user from token {}.",user);
		}
		if (Objects.isNull(user)) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		return user;
	}

	@Override
	public boolean getWelcomeMailTemplateDetails(String userEmail) {
		log.debug("getWelcomeMailTemplateDetails method start");
		Map model = new HashMap();
		model.put("userMail", userEmail);
		model.put("vstreemImage", vstreemImage);
		model.put("welcomeImage", welcomeImage);
		model.put("galleryImage", galleryImage);
		model.put("combistreemImage", combistreemImage);
		model.put("databondImage", databondImage);
		model.put("catalogueImage", catalogueImage);
		Email email = formEmailData.formEmail(formMail, userEmail, MessageConstants.WELCOME_TEMPLATE_SUBJECT, null,
				"WelcomeTemplate.html", model);
		try {
			mailService.sendEmail(email);
		} catch (Exception e) {
			throw new AppException("Something went wrong while sending mail, Please try again later.");
		}
		log.debug("getWelcomeMailTemplateDetails method end");

		return true;
	}

}
