package com.arraigntech.service.impl;

import java.io.IOException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import com.arraigntech.model.LoginResponseDTO;
import com.arraigntech.model.TokenResponse;
import com.arraigntech.model.UserDTO;
import com.arraigntech.model.UserToken;
import com.arraigntech.repository.EmailSettingsRepository;
import com.arraigntech.repository.ResetTokenRepository;
import com.arraigntech.repository.RoleRepository;
import com.arraigntech.repository.UserRespository;
import com.arraigntech.service.IVSService;
import com.arraigntech.service.MailService;
import com.arraigntech.utility.AuthenticationProvider;
import com.arraigntech.utility.CommonUtils;
import com.arraigntech.utility.EmailValidator;
import com.arraigntech.utility.FormEmailData;
import com.arraigntech.utility.IVSJwtUtil;
import com.arraigntech.utility.MessageConstants;
import com.arraigntech.utility.PasswordConstraintValidator;

import freemarker.template.TemplateException;

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
	
	public static final String vstreemImage= "https://s3.us-east-2.amazonaws.com/vstreem.com/assets/img/template-images/vestreem_logo.png";
	public static final String welcomeImage= "https://s3.us-east-2.amazonaws.com/vstreem.com/assets/img/template-images/welcome.jpg";
	public static final String galleryImage= "https://s3.us-east-2.amazonaws.com/vstreem.com/assets/img/template-images/gallery.png";
	public static final String databondImage= "https://s3.us-east-2.amazonaws.com/vstreem.com/assets/img/template-images/databond.png";
	public static final String combistreemImage= "https://s3.us-east-2.amazonaws.com/vstreem.com/assets/img/template-images/combistreem.png";
	public static final String catalogueImage= "https://s3.us-east-2.amazonaws.com/vstreem.com/assets/img/template-images/combistreem.png";

	public Boolean register(UserDTO userDTO) throws AppException {
		log.debug("register start");
		if (Objects.isNull(userDTO) || !StringUtils.hasText(userDTO.getUsername())
				|| !StringUtils.hasText(userDTO.getPassword())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		AuthenticationProvider provider = userDTO.getProvider();
		Boolean flag = false;
		if (userDTO.getEmail() == null || !emailValidator.isValidEmail(userDTO.getEmail())) {
			throw new AppException(MessageConstants.INVALID_EMAIL);
		}
		User newUser = userRepo.findByEmailAll(userDTO.getEmail());

		if (Objects.nonNull(newUser)) {
			throw new AppException(MessageConstants.USER_EXISTS);
		}
//		Optional<User> newUser1 = userRepo.findByUsernameAll(userDTO.getUsername());
//		if (newUser1.isPresent()) {
//			throw new AppException(MessageConstants.USER_EXISTS_USERNAME);
//		}
		if (!passwordValidator.isValid(userDTO.getPassword())) {
			throw new AppException(MessageConstants.INVALID_PASSWORD);
		}
//		if(provider.equals(null)) {
//			provider=AuthenticationProvider.LOCAL;
//		}
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
		Role role = roleRepo.findByName("ROLE_USER");
		newUser.getRoles().add(role);
		newUser.setEnabled(true);
		userRepo.save(newUser);
		if (Objects.isNull(newUser.getProvider())) {
			sendRegisterationLink(userDTO.getEmail());
		} else {
			getWelcomeMailTemplateDetails(userDTO.getEmail(),userDTO.getPassword());
		}
		log.debug("register end");
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
	public User create(User entity) {
		// TODO Auto-generated method stub
		return null;
	}

	public String updatePassword(String token, String newPassword) throws AppException {
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

	public LoginResponseDTO generateToken(LoginDetails login) throws AppException {
		log.debug("generateToken start");
		if (!StringUtils.hasText(login.getEmail()) || !StringUtils.hasText(login.getPassword())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if (!emailValidator.isValidEmail(login.getEmail())) {
			throw new AppException(MessageConstants.INVALID_EMAIL);
		}
		User newUser = userRepo.findByEmailAll(login.getEmail());
		if(newUser.getLoginCount() == 0) {
			newUser.setLoginCount(1);
		} else {
			newUser.setLoginCount(2);
		}
		userRepo.save(newUser);
		if (Objects.isNull(newUser)) {
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
			throw new AppException(MessageConstants.ACCOUNT_DISABLED);
		}
		if (!passwordEncoder.matches(login.getPassword(), newUser.getPassword())) {
			throw new AppException(MessageConstants.WRONG_PASSWORD);
		}

		if(newUser.isEmailVerified() && newUser.getLoginCount() == 1) {
			getWelcomeMailTemplateDetails(login.getEmail(),null);
		}
		OAuth2AccessToken accessToken = socialLoginService.getAccessToken(newUser);
//		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
//		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
//		headers.add("Authorization", basicAuth);
//
//		String localUrl = builder
//				.scheme(appScheme)
//				.host(appSchemeHost)
//				.path("/oauth/token").build().toUriString();
//		System.out.println(localUrl);
//		String uri = localUrl + "?grant_type=password&username=" + login.getEmail() + "&password="
//				+ login.getPassword();
//		System.out.println(uri);
//		HttpEntity<UserToken> request = new HttpEntity<>(headers);
//		TokenResponse response = null;
//		try {
//			ResponseEntity<TokenResponse> reponseEntity = restTemplate.exchange(uri, HttpMethod.POST, request,
//					TokenResponse.class);
//			response = reponseEntity.getBody();
//		} catch (Exception e) {
//			log.error("Failed to get access token for the given credentials");
//			throw new AppException(e.getMessage());
//		}
		log.debug("generateToken end");
		return new LoginResponseDTO(accessToken.toString(), true);

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
		Email emailDetails = formEmailData.formEmail(formMail, email,
				MessageConstants.RESET_PASSWORD_LINK, passwordResetLink, "ResetEmailTemplate.html", model);
		// Sending the mail with password reset link
		try {
			mailService.sendEmail(emailDetails);
		} catch (MessagingException | IOException | TemplateException e) {
			e.printStackTrace();
		}
		log.debug("forgotPassword end");
		return true;
	}

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
		userRepo.save(newUser);
		log.debug("updateAccountPassword end");
		return MessageConstants.PASSWORDMESSAGE;
	}

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

	public Boolean sendRegisterationLink(String userEmail) {
		log.debug("sendRegisterationLink method start");
		try {
			if (!StringUtils.hasText(userEmail) || !emailValidator.isValidEmail(userEmail)) {
				throw new AppException(MessageConstants.INVALID_EMAIL);
			}
			String token = jwtUtil.generateResetToken(userEmail, verficationMailExpirationTime);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance();

			String regisrationLink = builder.scheme("https").host(registrationBaseurl).path("/auth").queryParam("token", token)
					.buildAndExpand(token).toUriString();
			Map model = new HashMap();
			model.put("userMail", userEmail);
			model.put("regisrationLink", regisrationLink);
			model.put("vstreemImage", vstreemImage);
			Email email = formEmailData.formEmail(formMail, userEmail,
					MessageConstants.REGISTRATION_CONFIRMATION_LINK, regisrationLink, "VerificationEmailTemplate.ftl", model);
			User newUser=userRepo.findByEmailAll(userEmail);
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

	public Boolean verifyRegisterationToken(String token) {
		log.debug("verifyRegisterationToken method start");
		if (!StringUtils.hasText(token)) {
			throw new AppException(MessageConstants.INVALID_REGISTER_TOKEN);
		}
		if (!iVSJwtUtil.validateRegisterToken(token)) {
			return false;
		}
		String email = iVSJwtUtil.getUsernameFromToken(token);
		User user = userRepo.findByEmailAll(email);
		if (Objects.nonNull(user)) {
			user.setActive(true);
			user.setEmailVerified(true);
			userRepo.save(user);
			return true;
		}
		return false;
	}

	public Boolean welcomeMail(String email, String password) {
		try {
			mailService.sendWelcomeEmail(email, password);
		} catch (Exception e) {
			throw new AppException("Something went wrong while sending mail, Please try again later.");
		}
		return true;
	}

	private User getUser() {
		User user = userRepo.findByEmail(CommonUtils.getUser());
		if (Objects.isNull(user)) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		return user;
	}
	
	public boolean getWelcomeMailTemplateDetails(String userEmail, String password){
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
		Email email = formEmailData.formEmail(formMail, userEmail,
				MessageConstants.WWELCOME_TEMPLATE_SUBJECT, null, "WelcomeTemplate.ftl", model);
		try {
			mailService.sendEmail(email);
		} catch (Exception e) {
			throw new AppException("Something went wrong while sending mail, Please try again later.");
		}  
		log.debug("getWelcomeMailTemplateDetails method end");
		
		return true;
	}
	
}
