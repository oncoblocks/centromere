package org.oncoblocks.centromere.core.web.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by woemler on 2/23/15.
 */
public class RestException extends RuntimeException {

	private HttpStatus status;
	private Integer code;
	private String message;
	private String developerMessage;
	private String moreInfoUrl;

	public RestException(HttpStatus status, Integer code, String message, String developerMessage, String moreInfoUrl){
		if (status == null){
			throw new NullPointerException("HttpStatus argument cannot be null.");
		}
		this.status = status;
		this.code = code;
		this.message = message;
		this.developerMessage = developerMessage;
		this.moreInfoUrl = moreInfoUrl;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	@Override public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}

	public String getMoreInfoUrl() {
		return moreInfoUrl;
	}

	public void setMoreInfoUrl(String moreInfoUrl) {
		this.moreInfoUrl = moreInfoUrl;
	}

	public RestError getRestError(){
		return new RestError(this.status, this.code, this.message, this.developerMessage, this.moreInfoUrl);
	}
	
}
