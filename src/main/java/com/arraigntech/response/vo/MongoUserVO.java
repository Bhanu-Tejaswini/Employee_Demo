/**
 * 
 */
package com.arraigntech.response.vo;

/**
 * @author Bhaskara S
 *
 */
public class MongoUserVO {

	private String userId;
	private String email;
	private String username;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public MongoUserVO() {

	}

	public MongoUserVO(String userId, String email, String username) {
		super();
		this.userId = userId;
		this.email = email;
		this.username = username;
	}

	@Override
	public String toString() {
		return "MongoUser [userId=" + userId + ", email=" + email + ", username=" + username + "]";
	}
}
