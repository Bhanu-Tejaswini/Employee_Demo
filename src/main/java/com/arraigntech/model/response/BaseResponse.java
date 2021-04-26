package com.arraigntech.model.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arraigntech.entity.Channels;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5957134042349800463L;

	private Map<String, String> responseMessages = new HashMap<>();

	private boolean success;
	private T results;
	private boolean isCustomErrorHandling;

	public BaseResponse() {

	}

	public BaseResponse(T results) {
		this.results = results;
	}

	public boolean isSuccess() {
		return success;
	}

	public T getResults() {
		return results;
	}

	/**
	 * @return the responseMessages
	 */
	public Map<String, String> getResponseMessages() {
		return responseMessages;
	}

	public boolean getIsCustomErrorHandling() {
		return isCustomErrorHandling;
	}

	public BaseResponse<T> withCustomErrorHandling(Boolean isCustomErrorHandling) {
		this.isCustomErrorHandling = isCustomErrorHandling;
		return this;
	}

	public BaseResponse<T> withSuccess(boolean success) {
		this.success = success;
		return this;
	}

	public BaseResponse<T> withResponseMessage(String key, String message) {
		this.responseMessages.put(key, message);
		return this;
	}

	public BaseResponse<T> build() {
		return this;
	}



}
