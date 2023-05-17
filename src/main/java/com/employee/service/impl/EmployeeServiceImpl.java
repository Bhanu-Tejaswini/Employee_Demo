package com.employee.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.employee.entity.EmployeeEntity;
import com.employee.entity.RoleEntity;
import com.employee.exceptions.AppException;
import com.employee.repository.EmployeeRespository;
import com.employee.repository.ResetTokenRepository;
import com.employee.repository.RoleRepository;
import com.employee.request.vo.EmailVO;
import com.employee.request.vo.EmployeeVO;
import com.employee.request.vo.LoginDetailsVO;
import com.employee.response.vo.LoginResponseVO;
import com.employee.service.EmployeeService;
import com.employee.service.MailService;
import com.employee.utility.CommonUtils;
import com.employee.utility.EmailValidator;
import com.employee.utility.EmpJwtUtil;
import com.employee.utility.FormEmailData;
import com.employee.utility.MessageConstants;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRespository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepo;

	@Value("${basic.auth}")
	private String basicAuth;

	@Autowired
	private EmpJwtUtil jwtUtil;

	@Autowired
	private MailService mailService;


	@Autowired
	private EmailValidator emailValidator;


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
	private EmpJwtUtil empJwtUtil;

	public static final String ROLE_USER = "ROLE_USER";
	

	@Autowired
	private DefaultTokenServices tokenService;

	@Autowired
	private EmployeeService userService;

	@Value("${client-id}")
	private String clientId;

	@Autowired
	private UserDetailServiceImpl userDetailsService;


	private String getUsername(String value) {
		String[] newUsername = value.split(" ");
		String username = "";
		if (newUsername.length > 1) {
			for (int i = 0; i < newUsername.length; i++) {
				username = username + newUsername[i];
				if (i == newUsername.length - 1)
					break;
				username = username + "_";
			}
		} else {
			username = newUsername[0];
		}
		return username;
	}


	@Override
	public Boolean register(EmployeeVO userDTO) {
		if (Objects.isNull(userDTO) || !StringUtils.hasText(userDTO.getUsername())
				|| !StringUtils.hasText(userDTO.getPassword())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if (userDTO.getEmail() == null || !emailValidator.isValidEmail(userDTO.getEmail())) {
			throw new AppException(MessageConstants.INVALID_EMAIL);
		}
		EmployeeEntity newUser = userRepo.findByEmailAll(userDTO.getEmail());

		if (Objects.nonNull(newUser)) {
			throw new AppException(MessageConstants.USER_EXISTS);
		}


		newUser = new EmployeeEntity();
		newUser.setProvider(userDTO.getProvider());
		newUser.setUsername(userDTO.getUsername());
		newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		newUser.setEmail(userDTO.getEmail());
		newUser.setAccountNonExpired(true);
		newUser.setAccountNonLocked(true);
		newUser.setCredentialsNonExpired(true);
		newUser.setActive(false);
		RoleEntity role = roleRepo.findByName(ROLE_USER);
		newUser.getRoles().add(role);
		newUser.setEnabled(true);
		userRepo.save(newUser);
		if (Objects.isNull(newUser.getProvider())) {
			sendRegisterationLink(userDTO.getEmail());
		} 
		return true;
	}

	@Override
	public EmployeeEntity findByEmailAll(String email) {
		return userRepo.findByEmailAll(email);
	}

	@Override
	public void saveUser(EmployeeEntity newUser) {
		userRepo.save(newUser);
	}

	@Override
	public EmployeeEntity findByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Override
	public EmployeeEntity findByUsernameAndIdNot(String username, String id) {
		return userRepo.findByUsernameAndIdNot(username, id);
	}

	@Override
	public EmployeeEntity findByEmailAndIdNot(String email, String id) {
		return userRepo.findByEmailAndIdNot(email, id);
	}

	@Override
	public LoginResponseVO generateToken(LoginDetailsVO login) {
		if (!StringUtils.hasText(login.getEmail()) || !StringUtils.hasText(login.getPassword())) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if (!emailValidator.isValidEmail(login.getEmail())) {
			throw new AppException(MessageConstants.INVALID_EMAIL);
		}
		EmployeeEntity newUser = userRepo.findByEmailAll(login.getEmail());
		if (Objects.isNull(newUser)) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}

		if (!newUser.isActive()) {
			throw new AppException(MessageConstants.ACCOUNT_DISABLED);
		}
		if (!passwordEncoder.matches(login.getPassword(), newUser.getPassword())) {
			throw new AppException(MessageConstants.WRONG_PASSWORD);
		}
		OAuth2AccessToken accessToken = getAccessToken(newUser);
		return new LoginResponseVO(accessToken.toString(), true);

	}




	@Override
	public Boolean sendRegisterationLink(String userEmail) {
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
			EmailVO email = formEmailData.formEmail(formMail, userEmail, MessageConstants.REGISTRATION_CONFIRMATION_LINK,
					regisrationLink, "MailVerificationTemplate.html", model);
			EmployeeEntity newUser = findByEmailAll(userEmail);
			newUser.setUpdatedAt(new Date());
			userRepo.save(newUser);
			mailService.sendEmail(email);
		} catch (Exception e) {
			throw new AppException("Something went wrong, Please try again later.");
		}
		return true;
	}

	@Override
	public Boolean verifyRegisterationToken(String token) {
		if (!StringUtils.hasText(token)) {
			throw new AppException(MessageConstants.INVALID_REGISTER_TOKEN);
		}

		if (!empJwtUtil.validateRegisterToken(token)) {
			return false;
		}

		String email = empJwtUtil.getUsernameFromToken(token);
		EmployeeEntity user = findByEmailAll(email);
		if (Objects.nonNull(user)) {
			user.setActive(true);
			user.setEmailVerified(true);
			userRepo.save(user);
			return true;
		}
		return false;
	}


	public EmployeeEntity getUser() {
		EmployeeEntity user = findByEmail(CommonUtils.getUser());
		if (Objects.isNull(user)) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		return user;
	}
	
	public OAuth2AccessToken getAccessToken(EmployeeEntity user) {
		HashMap<String, String> authorizationParameters = new HashMap<String, String>();
		authorizationParameters.put("scope", "read");
		authorizationParameters.put("username", user.getEmail());
		authorizationParameters.put("client_id", clientId);
		authorizationParameters.put("grant", "password");

		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		user.getRoles().forEach((role) -> {
			role.getPermissions().forEach(permission -> {
				authorities.add(new SimpleGrantedAuthority(permission.getName()));
			});
		});

		Set<String> responseType = new HashSet<String>();
		responseType.add("password");

		Set<String> scopes = new HashSet<String>();
		scopes.add("read");
		scopes.add("write");

		OAuth2Request authorizationRequest = new OAuth2Request(authorizationParameters, clientId, authorities, true,
				scopes, null, "", responseType, null);

		EmployeeEntity userPrincipal = (EmployeeEntity) userDetailsService.loadUserByUsername(user.getEmail());
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal,
				null, authorities);

		OAuth2Authentication authenticationRequest = new OAuth2Authentication(authorizationRequest,
				authenticationToken);
		authenticationRequest.setAuthenticated(true);
		OAuth2AccessToken accessToken = tokenService.createAccessToken(authenticationRequest);
		return accessToken;
	}


}
