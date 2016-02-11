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

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Authenticates user requests using a previously generated {@link TokenDetails}.
 *   The token contains a hashed representation of the user's credentials and token creation and 
 *   expiration times, that allow it to be validated without the user having to submit their credentials
 *   every time a request is made, but still takes steps to prevent user impersonation and token
 *   forgery.
 * 
 * @author woemler
 */
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {
	
	private TokenOperations tokenOperations;
	private UserDetailsService userDetailsService;
	
	public AuthenticationTokenProcessingFilter(TokenOperations tokenOperations, 
			UserDetailsService userDetailsService){
		this.tokenOperations = tokenOperations;
		this.userDetailsService = userDetailsService;
	}

	@Override 
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (!(request instanceof HttpServletRequest)){
			throw new RuntimeException("Expecting an HTTP request.");
		}
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		String authToken = httpRequest.getHeader("X-Auth-Token");
		if (authToken == null){
			authToken = httpRequest.getParameter("token");
		}
		
		String username = tokenOperations.getUserNameFromToken(authToken);
		
		if (username != null){
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			if (tokenOperations.validateToken(authToken, userDetails)){
				UsernamePasswordAuthenticationToken authentication = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		
		chain.doFilter(request, response);
		
	}
	
}
