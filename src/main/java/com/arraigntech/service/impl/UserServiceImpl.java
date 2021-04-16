
package com.arraigntech.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.arraigntech.Exception.AppException;
import com.arraigntech.entity.Role;
import com.arraigntech.entity.User;
import com.arraigntech.model.LoginDetails;
import com.arraigntech.model.TokenResponse;
import com.arraigntech.model.UserDTO;
import com.arraigntech.model.UserToken;
import com.arraigntech.repository.RoleRepository;
import com.arraigntech.repository.UserRespository;
import com.arraigntech.service.IVSService;
import com.arraigntech.utility.IVSJwtUtil;
import com.arraigntech.utility.MessageConstants;

@Service
public class UserServiceImpl implements IVSService<User, String> {
	

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


	public Boolean register(UserDTO userDTO) throws AppException {
		User newUser=userRepo.findByEmail(userDTO.getEmail());
		
   		if(newUser!=null) {
			throw new AppException(MessageConstants.USER_EXISTS);
		}
   		newUser=new User();
		newUser.setUsername(userDTO.getUsername());
		newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		newUser.setEmail(userDTO.getEmail());
		newUser.setEnabled(true);
		newUser.setAccountNonExpired(true);
		newUser.setAccountNonLocked(true);
		newUser.setCredentialsNonExpired(true);
	
		
		for(String str:userDTO.getRole()) {
			Role role=roleRepo.findByName(str);
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
		if (newUser == null) {
			throw new AppException("The user already exists");
		}
		return userRepo.save(entity);
	}

	@Override
	public boolean delete(String id) throws AppException {
		Optional<User> newUser = userRepo.findById(id);
		if (!newUser.isPresent()) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		userRepo.deleteById(id);
		return true;
	}

	@Override
	public User create(User entity) {
		// TODO Auto-generated method stub
		return null;
	}

	public User updatePassword(String token, String newPassword)
			throws AppException {
		
		if(token.isEmpty() || newPassword.isEmpty()) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		if (!(StringUtils.hasText(token) && jwtUtil.validateToken(token))) {
			throw new AppException(MessageConstants.TOKEN_NOT_FOUND);
		}
		String email = jwtUtil.getUsernameFromToken(token);
		Date expiryDate = jwtUtil.getExpirationTimeFromToken(token);
		System.out.println("email" + email + "  expiry date" + expiryDate);
		User newUser = userRepo.findByEmail(email);
		if (newUser == null) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		newUser.setPassword(passwordEncoder.encode(newPassword));
		return userRepo.save(newUser);
	}
	
	public String generateToken(LoginDetails login, UriComponentsBuilder builder) throws AppException {
		if(login.getEmail().isEmpty() || login.getPassword().isEmpty()) {
			throw new AppException(MessageConstants.DETAILS_MISSING);
		}
		User newUser=userRepo.findByEmail(login.getEmail());
		if (newUser == null) {
			throw new AppException(MessageConstants.USER_NOT_FOUND);
		}
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Authorization",basicAuth);
		
		String localUrl=builder.path("/oauth/token").build().toUriString();
		
		String userName=newUser.getUsername();
		String uri=localUrl+"?grant_type=password&username="+userName+"&password="+login.getPassword();
		HttpEntity<UserToken> request = new HttpEntity<>(headers);
		ResponseEntity<TokenResponse> reponseEntity=restTemplate.exchange(uri, HttpMethod.POST, request, TokenResponse.class);
		TokenResponse response=reponseEntity.getBody();
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
}
