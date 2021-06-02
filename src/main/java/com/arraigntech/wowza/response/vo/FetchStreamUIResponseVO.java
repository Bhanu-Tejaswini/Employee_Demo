/**
 * 
 */
package com.arraigntech.wowza.response.vo;

/**
 * @author Bhaskara S
 *
 */
public class FetchStreamUIResponseVO {

	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public FetchStreamUIResponseVO(String status) {
		super();
		this.status = status;
	}

	public FetchStreamUIResponseVO() {

	}

	@Override
	public String toString() {
		return "FetchStreamUIResponse [status=" + status + "]";
	}
}
