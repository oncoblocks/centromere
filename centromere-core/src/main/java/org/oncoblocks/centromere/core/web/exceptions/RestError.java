package org.oncoblocks.centromere.core.web.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author woemler 
 */
public class RestError {

	private HttpStatus status;
	private Integer code;
	private String message;
	private String developerMessage;
	private String moreInfoUrl;

	public RestError(HttpStatus status, Integer code, String message, String developerMessage,
			String moreInfoUrl) {
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

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public String getMoreInfoUrl() {
		return moreInfoUrl;
	}
	
}
