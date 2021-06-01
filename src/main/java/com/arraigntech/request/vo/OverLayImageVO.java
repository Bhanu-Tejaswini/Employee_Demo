package com.arraigntech.request.vo;

import java.io.Serializable;

public class OverLayImageVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String overLayURL;
	private String overLayType;
	
	public String getOverLayURL() {
		return overLayURL;
	}
	public void setOverLayURL(String overLayURL) {
		this.overLayURL = overLayURL;
	}
	public String getOverLayType() {
		return overLayType;
	}
	public void setOverLayType(String overLayType) {
		this.overLayType = overLayType;
	}
	
	@Override
	public String toString() {
		return "OverLayImageVO [overLayURL=" + overLayURL + ", overLayType=" + overLayType + "]";
	}
}
