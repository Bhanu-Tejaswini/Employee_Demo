package com.arraigntech.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.arraigntech.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class LoggedInUserDetails implements UserDetails {
	
	private static final long serialVersionUID = -3291415360974416730L;

	private final String id;

	private String email;

	private String userName;

	@JsonIgnore
	private String password;


	final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

	public LoggedInUserDetails(UserEntity user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.userName = user.getUsername();
//		this.isActive = user.isActive();
//		this.isLocked = (user.getLockEndTime() != null && OffsetDateTime.now().isBefore(user.getLockEndTime()));
//		this.lastName = user.getLastName();
		this.password = user.getPassword();
//		this.passwordAttempt = user.getFailedLoginAttempt() != null ? user.getFailedLoginAttempt() : 0;
	};



	public LoggedInUserDetails() {
		this.id = "";
		// TODO Auto-generated constructor stub
	}



	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getId() {
		return this.id;
	};

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}



	@Override
	public String getUsername() {
		return userName;
	}



	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean isEnabled() {
		return true;
	}

}
