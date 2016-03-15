/*
 * Copyright 2016 William Oemler, Blueprint Medicines
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.oncoblocks.centromere.web.security;

import org.apache.commons.codec.binary.Hex;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

/**
 * Simple implementation of {@link TokenOperations} that
 *   creates a user authentication token from hashed credentials and authentication time stamps.
 * 
 * @author woemler
 */
public class BasicTokenUtils implements TokenOperations {
	
	private final String key;
	private Long tokenLifespan = 1000L * 60 * 60 * 24; // expires in one day

	public BasicTokenUtils(String key) {
		this.key = key;
	}

	/**
	 * {@link TokenOperations#createToken(UserDetails)}
	 */
	public String createToken(UserDetails userDetails){
		
		long expires = System.currentTimeMillis() + tokenLifespan;
		StringBuilder tokenBuilder = new StringBuilder();
		tokenBuilder.append(userDetails.getUsername());
		tokenBuilder.append(":");
		tokenBuilder.append(expires);
		tokenBuilder.append(":");
		tokenBuilder.append(computeSignature(userDetails, expires));
		
		return tokenBuilder.toString();
		
	}

	/**
	 * {@link TokenOperations#computeSignature(UserDetails, long)}
	 */
	public String computeSignature(UserDetails userDetails, long expires){
		
		StringBuilder signatureBuilder = new StringBuilder();
		signatureBuilder.append(userDetails.getUsername());
		signatureBuilder.append(":");
		signatureBuilder.append(expires);
		signatureBuilder.append(":");
		signatureBuilder.append(userDetails.getPassword());
		signatureBuilder.append(":");
		signatureBuilder.append(key);

		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e){
			throw new IllegalStateException("No MD5 algorithm available!");
		}
		
		return new String(Hex.encodeHex(digest.digest(signatureBuilder.toString().getBytes())));
		
	}

	/**
	 * {@link TokenOperations#getUserNameFromToken(String)}
	 */
	public String getUserNameFromToken(String authToken){
		
		if (null == authToken){
			return null;
		}
		String[] bits = authToken.split(":");
		return bits[0];
		
	}

	/**
	 * {@link TokenOperations#validateToken(String, UserDetails)}
	 */
	public boolean validateToken(String authToken, UserDetails userDetails){
		
		String[] bits = authToken.split(":");
		long expires = Long.parseLong(bits[1]);
		String signature = bits[2];
		
		if (expires < System.currentTimeMillis()){
			return false;
		}
		
		return signature.equals(computeSignature(userDetails, expires));
		
	}

	/**
	 * Sets the lifespan of the token in a time period defined by days.
	 * 
	 * @param days
	 */
	public void setTokenLifespanDays(Long days){
		Assert.notNull(days, "Number of days must not be null.");
		if (days < 1) throw new IllegalArgumentException("Number of days must be greater than zero.");
		tokenLifespan = 1000L * 60 * 60 * 24 * days;
	}

	/**
	 * Sets the lifespan of the token in a time period defined by hours.
	 * 
	 * @param hours
	 */
	public void setTokenLifespanHours(Long hours){
		Assert.notNull(hours, "Number of hours must not be null.");
		if (hours < 1) throw new IllegalArgumentException("Number of hours must be greater than zero.");
		tokenLifespan = 1000L * 60 * 60 * hours;
	}

	/**
	 * Creates a {@link TokenDetails} object, based upon submitted {@link UserDetails}.
	 * 
	 * @param userDetails
	 * @return
	 */
	public TokenDetails createTokenAndDetails(UserDetails userDetails){
		String token = this.createToken(userDetails);
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		calendar.add(Calendar.MILLISECOND, this.tokenLifespan.intValue());
		Date expires = calendar.getTime();
		return new TokenDetails(token, userDetails.getUsername(), now, expires);
	}
	
}
