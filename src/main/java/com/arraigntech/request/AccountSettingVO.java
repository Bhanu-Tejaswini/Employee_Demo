package com.arraigntech.request;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author tulabandula.kumar
 *
 */
public class AccountSettingVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String language;
	
	private List<String> timeZonesList;
	
	private List<String> countries;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<String> getTimeZonesList() {
		return timeZonesList;
	}

	public void setTimeZonesList(List<String> timeZonesList) {
		this.timeZonesList = timeZonesList;
	}

	public List<String> getCountries() {
		return countries;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}

	@Override
	public String toString() {
		return "AccountSettingVO [language=" + language + ", timeZonesList=" + timeZonesList + ", countries="
				+ countries + "]";
	}
}
