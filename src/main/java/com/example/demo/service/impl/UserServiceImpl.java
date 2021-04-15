package com.example.demo.service.impl;

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

import com.example.demo.Exception.DataExistsException;
import com.example.demo.Exception.DataNotFoundException;
import com.example.demo.Exception.UserTokenNotFoundException;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.model.LoginDetails;
import com.example.demo.model.TokenResponse;
import com.example.demo.model.UserDTO;
import com.example.demo.model.UserToken;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRespository;
import com.example.demo.service.IVSService;
import com.example.demo.utility.IVSJwtUtil;

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

	public Boolean register(UserDTO userDTO) {
		User newUser = new User();
//		newUser=userRepo.findByEmail(userDTO.getEmail());
//   		if(newUser!=null) {
//			throw new DataExistsException("The user already exists");
//		}
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
	public List<User> getAll() throws DataNotFoundException {
		List<User> users = userRepo.findAll();
		if (users.isEmpty()) {
			throw new DataNotFoundException("The Users list is empty");
		}
		return users;
	}

	@Override
	public User update(User entity) throws DataExistsException {
		User newUser = new User();
		newUser = userRepo.findByEmail(entity.getEmail());
		if (newUser == null) {
			throw new DataExistsException("The user already exists");
		}
		return userRepo.save(entity);
	}

	@Override
	public boolean delete(String id) throws DataNotFoundException {
		Optional<User> newUser = userRepo.findById(id);
		if (!newUser.isPresent()) {
			throw new DataNotFoundException("The User does not exists");
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
			throws UserTokenNotFoundException, DataNotFoundException {
		if (!(StringUtils.hasText(token) && jwtUtil.validateToken(token))) {
			throw new UserTokenNotFoundException("Token does not exists");
		}
		String email = jwtUtil.getUsernameFromToken(token);
		Date expiryDate = jwtUtil.getExpirationTimeFromToken(token);
		System.out.println("email" + email + "  expiry date" + expiryDate);
		User newUser = userRepo.findByEmail(email);
		if (newUser == null) {
			throw new DataNotFoundException("Unable to find the user with the provided email" + email);
		}
		newUser.setPassword(passwordEncoder.encode(newPassword));
		return userRepo.save(newUser);
	}
	
	public String generateToken(LoginDetails login, UriComponentsBuilder builder) {
		User newUser=userRepo.findByEmail(login.getEmail());
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
	public User getById(String id) throws DataNotFoundException {
		Optional<User> newUser = userRepo.findById(id);
		if (!newUser.isPresent()) {
			throw new DataNotFoundException("The mentioned user not available");
		}
		return newUser.get();
	}
}
