/*
 * Copyright 2015 William Oemler, Blueprint Medicines
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Simple implementation of {@link TokenOperations} that
 *   creates a user authentication token from hashed credentials and authentication time stamps.
 * 
 * @author woemler
 */
public class BasicTokenUtils implements TokenOperations {
	
	private String key;

	public BasicTokenUtils(String key) {
		this.key = key;
	}

	@Override
	public String createToken(UserDetails userDetails){
		
		long expires = System.currentTimeMillis() + (1000L * 60 * 60 * 24); // expires in one day
		StringBuilder tokenBuilder = new StringBuilder();
		tokenBuilder.append(userDetails.getUsername());
		tokenBuilder.append(":");
		tokenBuilder.append(expires);
		tokenBuilder.append(":");
		tokenBuilder.append(computeSignature(userDetails, expires));
		
		return tokenBuilder.toString();
		
	}
	
	@Override
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
	
	@Override
	public String getUserNameFromToken(String authToken){
		
		if (null == authToken){
			return null;
		}
		String[] bits = authToken.split(":");
		return bits[0];
		
	}
	
	@Override
	public boolean validateToken(String authToken, UserDetails userDetails){
		
		String[] bits = authToken.split(":");
		long expires = Long.parseLong(bits[1]);
		String signature = bits[2];
		
		if (expires < System.currentTimeMillis()){
			return false;
		}
		
		return signature.equals(computeSignature(userDetails, expires));
		
	}
	
}
