package com.arraigntech.utility;
/**
 * 
 * @author tulabandula.kumar
 *
 */
public enum UtilEnum {
	
	ENGLISH("English");
	
	private final String Name;
	
	UtilEnum(String Name) {
		this.Name = Name;
	}

	public String getLanguageName() {
		return Name;
	}
	
	

}
