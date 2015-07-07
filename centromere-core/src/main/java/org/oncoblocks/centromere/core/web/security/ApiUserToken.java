package org.oncoblocks.centromere.core.web.security;

import java.util.Date;

/**
 * Simple representation of a user authentication token used to verify user credentials in a stateless
 *   manner.  
 * 
 * @author woemler
 */
public class ApiUserToken {
	
	private String token;
	private String username;
	private Date issued;
	private Date expires;

	public ApiUserToken() { }

	public ApiUserToken(String token, String username, Date issueDate, Date expirationDate) {
		this.token = token;
		this.username = username;
		this.issued = issueDate;
		this.expires = expirationDate;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getIssued() {
		return issued;
	}

	public void setIssued(Date issued) {
		this.issued = issued;
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}
}
