package com.arraigntech.model;

public class UserSettingsDTO {

	private String email;
	private String mobilenumber;
	private String pincode;
	private String username;
	private String language;
	private String timezone;
	
	private String code;

	public UserSettingsDTO() {

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilenumber() {
		return mobilenumber;
	}

	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public UserSettingsDTO(String email, String mobilenumber, String pincode, String username, String language,
			String timezone) {
		super();
		this.email = email;
		this.mobilenumber = mobilenumber;
		this.pincode = pincode;
		this.username = username;
		this.language = language;
		this.timezone = timezone;
	}

}
