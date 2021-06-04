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

	public S3UIResponse(String imageUrl, String imageId) {
		super();
		this.imageUrl = imageUrl;
		this.imageId = imageId;
	}

	public S3UIResponse(String imageUrl) {
		super();
		this.imageUrl = imageUrl;
	}

	public S3UIResponse() {

	}
}
