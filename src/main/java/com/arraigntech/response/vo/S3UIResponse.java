/**
 * 
 */
package com.arraigntech.response.vo;

/**
 * @author Bhaskara S
 *
 */
public class S3UIResponse {

	private String imageUrl;
	private String imageId;
	private boolean active;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
	

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public S3UIResponse(String imageUrl, String imageId, boolean active) {
		super();
		this.imageUrl = imageUrl;
		this.imageId = imageId;
		this.active = active;
	}

	public S3UIResponse(String imageUrl) {
		super();
		this.imageUrl = imageUrl;
	}

	public S3UIResponse() {

	}
}
