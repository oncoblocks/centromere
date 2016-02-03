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

package org.oncoblocks.centromere.web.config;

import org.oncoblocks.centromere.web.security.AuthenticationTokenProcessingFilter;
import org.oncoblocks.centromere.web.security.BasicTokenUtils;
import org.oncoblocks.centromere.web.security.TokenOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Default security configuration for Centromere web services.  
 * 
 * @author woemler
 */
@Configuration
@EnableWebSecurity
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

	@SuppressWarnings("SpringJavaAutowiringInspection") @Autowired
	private UserDetailsService userService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.userDetailsService(userService)
				.passwordEncoder(new BCryptPasswordEncoder());
	}

	@Configuration
	@Order(1)
	public static class ApiTokenAuthenticationConfig extends WebSecurityConfigurerAdapter {

		@SuppressWarnings("SpringJavaAutowiringInspection") @Autowired
		private UserDetailsService userService;

		@Autowired
		private Environment env;

		@Bean
		public TokenOperations tokenUtils() {
			BasicTokenUtils tokenUtils = new BasicTokenUtils(env.getRequiredProperty("centromere.security.token"));
			try {
				tokenUtils.setTokenLifespanHours(Long.parseLong(env.getRequiredProperty(
						"centromere.security.token-lifespan-hours")));
			} catch (NumberFormatException e){
				try {
					tokenUtils.setTokenLifespanDays(Long.parseLong(env.getRequiredProperty(
							"centromere.security.token-lifespan-days")));
				} catch (NumberFormatException ex){
					logger.warn("[CENTROMERE] Token lifespan not properly configured.  Reverting to default configuration");
					tokenUtils.setTokenLifespanDays(1L);
				}
			}
			return tokenUtils;
		}

		@Bean
		public AuthenticationTokenProcessingFilter authenticationTokenProcessingFilter() {
			return new AuthenticationTokenProcessingFilter(tokenUtils(), userService);
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			
			String secureUrl = env.getRequiredProperty("centromere.security.secure-url");
			Boolean secureRead = Boolean.parseBoolean(env.getRequiredProperty(
					"centromere.security.secure-read"));
			Boolean secureWrite = Boolean.parseBoolean(env.getRequiredProperty(
					"centromere.security.secure-write"));
			
			logger.info(String.format("[CENTROMERE] Secure URL: %s", secureUrl));
			logger.info(String.format("[CENTROMERE] Secure Read: %s", secureRead.toString()));
			logger.info(String.format("[CENTROMERE] Secure Write: %s", secureWrite.toString()));
			
			if (secureRead && secureWrite){
				http
						.sessionManagement()
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
						.and()
						.addFilterBefore(authenticationTokenProcessingFilter(),
								UsernamePasswordAuthenticationFilter.class)
						.antMatcher(secureUrl)
						.authorizeRequests()
						.antMatchers(HttpMethod.GET, secureUrl).fullyAuthenticated()
						.antMatchers(HttpMethod.POST, secureUrl).fullyAuthenticated()
						.antMatchers(HttpMethod.PUT, secureUrl).fullyAuthenticated()
						.antMatchers(HttpMethod.DELETE, secureUrl).fullyAuthenticated()
						.antMatchers(HttpMethod.PATCH, secureUrl).fullyAuthenticated()
						.antMatchers(HttpMethod.OPTIONS, secureUrl).fullyAuthenticated()
						.antMatchers(HttpMethod.HEAD, secureUrl).fullyAuthenticated()
						.and()
						.csrf().disable();
			} else if (!secureRead && !secureWrite) {
				http
						.sessionManagement()
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
						.and()
						.addFilterBefore(authenticationTokenProcessingFilter(),
								UsernamePasswordAuthenticationFilter.class)
						.antMatcher(secureUrl)
						.authorizeRequests()
						.antMatchers(HttpMethod.GET, secureUrl).permitAll()
						.antMatchers(HttpMethod.POST, secureUrl).permitAll()
						.antMatchers(HttpMethod.PUT, secureUrl).permitAll()
						.antMatchers(HttpMethod.DELETE, secureUrl).permitAll()
						.antMatchers(HttpMethod.PATCH, secureUrl).permitAll()
						.antMatchers(HttpMethod.OPTIONS, secureUrl).permitAll()
						.antMatchers(HttpMethod.HEAD, secureUrl).permitAll()
						.and()
						.csrf().disable();
			} else {
				http
						.sessionManagement()
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
						.and()
						.addFilterBefore(authenticationTokenProcessingFilter(),
								UsernamePasswordAuthenticationFilter.class)
						.antMatcher(secureUrl)
						.authorizeRequests()
						.antMatchers(HttpMethod.GET, secureUrl).permitAll()
						.antMatchers(HttpMethod.POST, secureUrl).fullyAuthenticated()
						.antMatchers(HttpMethod.PUT, secureUrl).fullyAuthenticated()
						.antMatchers(HttpMethod.DELETE, secureUrl).fullyAuthenticated()
						.antMatchers(HttpMethod.PATCH, secureUrl).fullyAuthenticated()
						.antMatchers(HttpMethod.OPTIONS, secureUrl).permitAll()
						.antMatchers(HttpMethod.HEAD, secureUrl).permitAll()
						.and()
						.csrf().disable();
			}
		}
	}


	@Configuration
	@Order(2)
	public static class BasicWebSecurtiyConfig extends WebSecurityConfigurerAdapter {
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
					.authorizeRequests()
							.anyRequest().permitAll()
							.and()
					.sessionManagement()
							.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
							.and()
					.httpBasic()
							.and()
					.csrf().disable();
		}
	}

}
