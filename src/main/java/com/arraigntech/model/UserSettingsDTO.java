package com.arraigntech.model;

import java.util.Map;

public class UserSettingsDTO {

	private String email;
	private String mobilenumber;
	private String pincode;
	private String username;
	private String language;
	private String timezone;
	private String dialCode;
	private String countryCode;
	private String e164Number;
	private String internationalNumber;
	private String nationalNumber;
	private String number;
	private String code;
	private Map<String, String> mobileNumbersMap;

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

	public String getE164Number() {
		return e164Number;
	}

	public void setE164Number(String e164Number) {
		this.e164Number = e164Number;
	}

	public String getInternationalNumber() {
		return internationalNumber;
	}

	public void setInternationalNumber(String internationalNumber) {
		this.internationalNumber = internationalNumber;
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

	public Map<String, String> getMobileNumbersMap() {
		return mobileNumbersMap;
	}

	public void setMobileNumbersMap(Map<String, String> mobileNumbersMap) {
		this.mobileNumbersMap = mobileNumbersMap;
	}

	public UserSettingsDTO(String email, String pincode, String username, String language, String timezone,
			Map<String, String> mobileNumbersMap) {
		super();
		this.email = email;
		this.pincode = pincode;
		this.username = username;
		this.language = language;
		this.timezone = timezone;
		this.mobileNumbersMap = mobileNumbersMap;
	}

	@Override
	public String toString() {
		return "UserSettingsDTO [email=" + email + ", mobilenumber=" + mobilenumber + ", pincode=" + pincode
				+ ", username=" + username + ", language=" + language + ", timezone=" + timezone + ", dialCode="
				+ dialCode + ", countryCode=" + countryCode + ", e164Number=" + e164Number + ", internationalNumber="
				+ internationalNumber + ", nationalNumber=" + nationalNumber + ", number=" + number + ", code=" + code
				+ ", mobileNumbersMap=" + mobileNumbersMap + "]";
	}
}
