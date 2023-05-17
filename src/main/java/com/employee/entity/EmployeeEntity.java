package com.employee.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.employee.utility.AuthenticationProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EMPLOYEE")
public class EmployeeEntity  {

	public EmployeeEntity(EmployeeEntity user) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.email = user.getEmail();
		this.enabled = user.isEnabled();
		this.accountNonExpired = user.isAccountNonExpired();
		this.credentialsNonExpired = user.isCredentialsNonExpired();
		this.accountNonLocked = user.isAccountNonLocked();
		this.roles = user.getRoles();
	}
	
	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	protected String id;


	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
	@Column(name = "email")
	private String email;
	@Column(name = "enabled")
	@Type(type = "numeric_boolean")
	private boolean enabled;
	@Column(name = "accountnonexpired")
	@Type(type = "numeric_boolean")
	private boolean accountNonExpired;
	@Column(name = "credentialsnonexpired")
	@Type(type = "numeric_boolean")
	private boolean credentialsNonExpired;
	@Column(name = "accountnonlocked")
	@Type(type = "numeric_boolean")
	private boolean accountNonLocked;
	@Column
	private String pincode;
	@Column
	private String language;
	@Column(name = "time_zone")
	private String timeZone;
	@Column(name = "dial_code")
	private String dialCode;
	@Column(name = "country_code")
	private String countryCode;
	@Column(name = "national_number")
	private String nationalNumber;
	@Column(name = "number")
	private String number;
	private String code;
	@Column
	@Type(type = "numeric_boolean")
	private boolean active;
	@Enumerated(EnumType.STRING)
	@Column
	private AuthenticationProvider provider;

	private OffsetDateTime verificationEndTime;

	private String otp;

	@Column(name = "email_verified")
	@Type(type = "numeric_boolean")
	private boolean emailVerified;
	

	@JsonIgnore
	protected Date createdAt;

	@JsonIgnore
	protected Date updatedAt;

	@JsonIgnore
	protected String createdBy;
	@JsonIgnore
	protected String updatedBy;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role_user", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private List<RoleEntity> roles = new ArrayList<>();

	public EmployeeEntity() {

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public AuthenticationProvider getProvider() {
		return provider;
	}

	public void setProvider(AuthenticationProvider provider) {
		this.provider = provider;
	}


	public OffsetDateTime getVerificationEndTime() {
		return verificationEndTime;
	}

	public void setVerificationEndTime(OffsetDateTime verificationEndTime) {
		this.verificationEndTime = verificationEndTime;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public List<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public String getDialCode() {
		return dialCode;
	}

	public void setDialCode(String dialCode) {
		this.dialCode = dialCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}


	public String getNationalNumber() {
		return nationalNumber;
	}

	public void setNationalNumber(String nationalNumber) {
		this.nationalNumber = nationalNumber;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	
}
