/**
 * 
 */
package com.arraigntech.response;

/**
 * @author Bhaskara S
 *
 */
public class FacebookLongLivedTokenResponseVO {

	private String access_token;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	@Override
	public String toString() {
		return "FacebookLongLivedTokenResponse [access_token=" + access_token + "]";
	}
}
