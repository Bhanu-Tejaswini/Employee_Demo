/**
 * 
 */
package com.arraigntech.streams.model;

/**
 * @author Bhaskara S
 *
 */
public class FetchStreamUIResponse {

	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public FetchStreamUIResponse(String status) {
		super();
		this.status = status;
	}

	public FetchStreamUIResponse() {

	}

	@Override
	public String toString() {
		return "FetchStreamUIResponse [status=" + status + "]";
	}
}
