package com.arraigntech.model;

import java.util.Arrays;

public class ImageModel {
	
	private String aPath;
	private String aName;
	private byte[] imageBytes;

	public String getaPath() {
	    return aPath;
	}
	public void setaPath(String aPath) {
	    this.aPath = aPath;
	}
	public String getaName() {
	    return aName;
	}
	public void setaName(String aName) {
	    this.aName = aName;
	 }
	public byte[] getImageBytes() {
		return imageBytes;
	}
	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}
	
	@Override
	public String toString() {
		return "ImageModel [aPath=" + aPath + ", aName=" + aName + ", imageBytes=" + Arrays.toString(imageBytes) + "]";
	}
}
