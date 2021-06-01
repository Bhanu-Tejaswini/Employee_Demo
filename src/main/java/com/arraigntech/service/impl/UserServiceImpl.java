package com.arraigntech.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
		if(log.isDebugEnabled()) {
			log.debug("register medthod start {}.",userDTO);
		}
		if (Objects.isNull(userDTO) || !StringUtils.hasText(userDTO.getUsername())
				|| !StringUtils.hasText(userDTO.getPassword())) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.DETAILS_MISSING);
			}
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if (userDTO.getEmail() == null || !emailValidator.isValidEmail(userDTO.getEmail())) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.INVALID_EMAIL);
			}
			throw new AppException(MessageConstants.INVALID_EMAIL);
		}
		User newUser = userRepo.findByEmailAll(userDTO.getEmail());

		if (Objects.nonNull(newUser)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.USER_EXISTS);
			}
			throw new AppException(MessageConstants.USER_EXISTS);
		}
		if (!passwordValidator.isValid(userDTO.getPassword())) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.INVALID_PASSWORD);
			}
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
		if(log.isDebugEnabled()) {
			log.debug("register medthod start {}.",userDTO);
		}
		log.debug("register end");
		return true;
	}

	@Override
	public User findByEmailAll(String email) {
		if(log.isDebugEnabled()) {
			log.debug("findByEmailAll medthod {}",email);
		}
		return userRepo.findByEmailAll(email);
	}

	@Override
	public void saveUser(User newUser) {
		if(log.isDebugEnabled()) {
			log.debug("saveUser medthod {}",newUser);
		}
		userRepo.save(newUser);
	}

	@Override
	public User findByEmail(String email) {
		if(log.isDebugEnabled()) {
			log.debug("findByEmail medthod {}",email);
		}
		return userRepo.findByEmail(email);
	}

	@Override
	public User findByUsernameAndIdNot(String username, String id) {
		if(log.isDebugEnabled()) {
			log.debug("findByUsernameAndIdNot medthod");
		}
		return userRepo.findByUsernameAndIdNot(username, id);
	}

	@Override
	public User findByEmailAndIdNot(String email, String id) {
		if(log.isDebugEnabled()) {
			log.debug("findByEmailAndIdNot medthod");
		}
		return userRepo.findByEmailAndIdNot(email, id);
	}

	@Override
	public boolean delete(String password) {
		if(log.isDebugEnabled()) {
			log.debug("delete method start");
		}
		User newUser = getUser();
		if (passwordEncoder.matches(password, newUser.getPassword())) {
			newUser.setActive(false);
		} else {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.WRONG_PASSWORD);
			}
			throw new AppException(MessageConstants.WRONG_PASSWORD);
		}
		userRepo.save(newUser);
		if(log.isDebugEnabled()) {
			log.debug("delete method end");
		}
		return true;
	}

	@Override
	public String updatePassword(String token, String newPassword) {
		if(log.isDebugEnabled()) {
			log.debug("updatePassword method start");
		}
		if (!StringUtils.hasText(token) || !StringUtils.hasText(newPassword)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.DETAILS_MISSING);
			}
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if (!passwordValidator.isValid(newPassword)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.INVALID_PASSWORD);
			}
			throw new AppException(MessageConstants.INVALID_PASSWORD);
		}
		if (!(StringUtils.hasText(token) && jwtUtil.validateToken(token))) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.TOKEN_NOT_FOUND);
			}
			throw new AppException(MessageConstants.TOKEN_NOT_FOUND);
		}
		String email = jwtUtil.getUsernameFromToken(token);

		User newUser = userRepo.findByEmail(email);
		if (Objects.isNull(newUser)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.USER_NOT_FOUND);
			}
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		// check whether token exist in the database or not
		ResetToken resetToken = resetTokenRepo.findByUser(newUser);
		if (Objects.isNull(resetToken)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.TOKEN_EXPIRED);
			}
			throw new AppException(MessageConstants.TOKEN_EXPIRED);
		}
		newUser.setPassword(passwordEncoder.encode(newPassword));
		userRepo.save(newUser);
		// deleting the token after reseting the password
		resetTokenRepo.deleteById(resetToken.getId());
		log.debug(email);
		if(log.isDebugEnabled()) {
			log.debug("updatePassword method end");
		}
		return MessageConstants.PASSWORDMESSAGE;
	}

	@Override
	public LoginResponseDTO generateToken(LoginDetails login) {
		if(log.isDebugEnabled()) {
			log.debug("generateToken method start {}.",login);
		}
		log.debug("generateToken start");
		if (!StringUtils.hasText(login.getEmail()) || !StringUtils.hasText(login.getPassword())) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.DETAILS_MISSING);
			}
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if (!emailValidator.isValidEmail(login.getEmail())) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.INVALID_EMAIL);
			}
			throw new AppException(MessageConstants.INVALID_EMAIL);
		}
		User newUser = userRepo.findByEmailAll(login.getEmail());
		if (Objects.isNull(newUser)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.USER_NOT_FOUND);
			}
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}

		if (!newUser.isEmailVerified()) {
			if (System.currentTimeMillis() < newUser.getUpdatedAt().getTime() + 900000) {
				return new LoginResponseDTO(MessageConstants.VERIFICATION_MAIL_ALREADYSENT, false);
			} else {
				sendRegisterationLink(login.getEmail());
				return new LoginResponseDTO(MessageConstants.TOKEN_EXPIRED_RESENDMAIL, false);
			}
		}
		if (!newUser.isActive()) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.ACCOUNT_DISABLED);
			}
			throw new AppException(MessageConstants.ACCOUNT_DISABLED);
		}
		if (!passwordEncoder.matches(login.getPassword(), newUser.getPassword())) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.WRONG_PASSWORD);
			}
			throw new AppException(MessageConstants.WRONG_PASSWORD);
		}
		OAuth2AccessToken accessToken = socialLoginService.getAccessToken(newUser);
		log.debug("generateToken end");
		if(log.isDebugEnabled()) {
			log.debug("generateToken method end");
		}
		return new LoginResponseDTO(accessToken.toString(), true);

	}

	@Override
	public Boolean forgotPassword(String email) {
		if(log.isDebugEnabled()) {
			log.debug("forgotPassword method start {}.",email);
		}
		log.debug("forgotPassword start");
		if (!StringUtils.hasText(email) || !emailValidator.isValidEmail(email)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.INVALID_EMAIL);
			}
			throw new AppException(MessageConstants.INVALID_EMAIL);
		}
		User newUser = userRepo.findByEmailAll(email);
		if (Objects.isNull(newUser)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.USER_NOT_FOUND);
			}
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		if (!newUser.isActive()) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.ACCOUNT_DISABLED);
			}
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
		} catch (Exception e) {
			if(log.isDebugEnabled()) {
				log.error("Something went wrong while sending mail, Please try again later.");
			}
			throw new AppException("Something went wrong while sending mail, Please try again later.");
		}
		if(log.isDebugEnabled()) {
			log.debug("forgotPassword method end");
		}
		return true;
	}

	@Override
	public String updateAccountPassword(String oldPassword, String newPassword) {
		if(log.isDebugEnabled()) {
			log.debug("updateAccountPassword method start");
		}
		log.debug("updateAccountPassword start");
		if (!StringUtils.hasText(oldPassword) || !StringUtils.hasText(newPassword)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.DETAILS_MISSING);
			}
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if (!passwordValidator.isValid(newPassword)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.INVALID_PASSWORD);
			}
			throw new AppException(MessageConstants.INVALID_PASSWORD);
		}
		User newUser = getUser();
		if (passwordEncoder.matches(oldPassword, newUser.getPassword())) {
			newUser.setPassword(passwordEncoder.encode(newPassword));
		} else {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.WRONG_PASSWORD);
			}
			throw new AppException(MessageConstants.WRONG_PASSWORD);
		}
		saveUser(newUser);
		if(log.isDebugEnabled()) {
			log.debug("updateAccountPassword method end");
		}
		return MessageConstants.PASSWORDMESSAGE;
	}

	@Override
	public EmailSettingsModel getEmailSetting() {
		if(log.isDebugEnabled()) {
			log.debug("getEmailSettings method start");
		}
		User newUser = getUser();
		EmailSettings emailSettings = emailSettingsRepo.findByUser(newUser);
		if (Objects.isNull(emailSettings)) {
			return new EmailSettingsModel(true, true, true, true, true, true);
		}
		if(log.isDebugEnabled()) {
			log.debug("getEmailSettings method end");
		}
		return new EmailSettingsModel(emailSettings.isSystemAlerts(), emailSettings.isMonthlyStreamingReports(),
				emailSettings.isInstantStreamingReport(), emailSettings.isPromotions(),
				emailSettings.isProductUpdates(), emailSettings.isBlogDigest());
	}

	@Override
	public String saveEmailSettings(EmailSettingsModel emailSettings) {
		if(log.isDebugEnabled()) {
			log.debug("saveEmailSettings method start {}.",emailSettings);
		}
		if (Objects.isNull(emailSettings)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.DETAILS_MISSING);
			}
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
		if(log.isDebugEnabled()) {
			log.debug("saveEmailSettings method end");
		}
		return MessageConstants.EMAILSETTINGSMESSAGE;
	}

	@Override
	public boolean persistEmailSettings(EmailSettings settings, EmailSettingsModel emailSettings, User user) {
		if(log.isDebugEnabled()) {
			log.debug("persistEmailSettings method start");
		}
		settings.setUser(user);
		settings.setSystemAlerts(emailSettings.getSystemAlerts());
		settings.setMonthlyStreamingReports(emailSettings.getMonthlyStreamingReports());
		settings.setInstantStreamingReport(emailSettings.getInstantStreamingReport());
		settings.setPromotions(emailSettings.getPromotions());
		settings.setProductUpdates(emailSettings.getProductUpdates());
		settings.setBlogDigest(emailSettings.getBlogDigest());
		emailSettingsRepo.save(settings);
		if(log.isDebugEnabled()) {
			log.debug("persistEmailSettings method end");
		}
		return true;
	}

	@Override
	public Boolean sendRegisterationLink(String userEmail) {
		if(log.isDebugEnabled()) {
			log.debug("sendRegisterationLink method start {}",userEmail);
		}
		
		log.debug("sendRegisterationLink method start");
		try {
			if (!StringUtils.hasText(userEmail) || !emailValidator.isValidEmail(userEmail)) {
				if(log.isDebugEnabled()) {
					log.error(MessageConstants.INVALID_EMAIL);
				}
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
			if(log.isDebugEnabled()) {
				log.debug("sendRegisterationLink method end");
			}
		} catch (Exception e) {
			if(log.isDebugEnabled()) {
				log.error("Error in sending registration link : " + userEmail, e);
			}
			throw new AppException("Something went wrong, Please try again later.");
		}
		return true;
	}

	@Override
	public Boolean verifyRegisterationToken(String token) {
		if(log.isDebugEnabled()) {
			log.debug("verifyRegisterationToken method start {}",token);
		}
		if (!StringUtils.hasText(token)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.INVALID_REGISTER_TOKEN);
			}
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
		if(log.isDebugEnabled()) {
			log.debug("verifyRegisterationToken method end");
		}
		return false;
	}

	@Override
	public Boolean welcomeMail(String userEmail, String password) {
		if(log.isDebugEnabled()) {
			log.debug("welcomeMail method start");
		}
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
			if(log.isDebugEnabled()) {
				log.error("Something went wrong while sending mail, Please try again later");
			}
			throw new AppException("Something went wrong while sending mail, Please try again later.");
		}
		if(log.isDebugEnabled()) {
			log.debug("welcomeMail method end");
		}
		return true;
	}

	public User getUser() {
		User user = findByEmail(CommonUtils.getUser());
		if(log.isDebugEnabled()) {
			 log.debug("getting the user from token {}.",user);
		}
		if (Objects.isNull(user)) {
			if(log.isDebugEnabled()) {
				log.error(MessageConstants.USER_NOT_FOUND);
			}
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		return user;
	}

	@Override
	public boolean getWelcomeMailTemplateDetails(String userEmail) {
		if(log.isDebugEnabled()) {
			log.debug("getWelcomeMailTemplateDetails method start {}",userEmail);
		}
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
			if(log.isDebugEnabled()) {
				log.error("Something went wrong while sending mail, Please try again later.");
			}
			throw new AppException("Something went wrong while sending mail, Please try again later.");
		}
		if(log.isDebugEnabled()) {
			log.debug("getWelcomeMailTemplateDetails method end");
		}
		return true;
	}

}
