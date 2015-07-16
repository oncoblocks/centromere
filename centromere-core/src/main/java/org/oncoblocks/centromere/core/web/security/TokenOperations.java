package org.oncoblocks.centromere.core.web.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Standard method interface for creating and validating user {@link org.oncoblocks.centromere.core.web.security.ApiUserToken}
 * 
 * @author woemler
 */
public interface TokenOperations {

	/**
	 * Generates a string token from the submitted {@link org.springframework.security.core.userdetails.UserDetails}
	 * 
	 * @param userDetails {@link org.springframework.security.core.userdetails.UserDetails}
	 * @return string token representation
	 */
	String createToken(UserDetails userDetails);

	/**
	 * Creates the hash of user credentials and token expiration timestamp.
	 * 
	 * @param userDetails {@link org.springframework.security.core.userdetails.UserDetails}
	 * @param expires timestamp (in milliseconds) when the token expires.
	 * @return string representation of the hash
	 */
	String computeSignature(UserDetails userDetails, long expires);

	/**
	 * Returns the username portion of a submitted authentication token
	 * 
	 * @param authToken token generated by {@link TokenOperations#createToken}
	 * @return username
	 */
	String getUserNameFromToken(String authToken);

	/**
	 * Checks that the submitted token is valid and returns a {@code boolean} verdict
	 * 
	 * @param authToken token generated by {@link TokenOperations#createToken}
	 * @param userDetails {@link org.springframework.security.core.userdetails.UserDetails}
	 * @return boolean result of validation
	 */
	boolean validateToken(String authToken, UserDetails userDetails);
}