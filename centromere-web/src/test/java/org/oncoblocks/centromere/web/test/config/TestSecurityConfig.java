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

package org.oncoblocks.centromere.web.test.config;

/**
 * @author woemler
 */

import org.oncoblocks.centromere.web.config.WebSecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(WebSecurityConfig.class)
public class TestSecurityConfig {}

//@Configuration
//@EnableWebSecurity
//@EnableWebMvcSecurity
//public class TestSecurityConfig extends WebSecurityConfigurerAdapter {
//
//	@Autowired private UserDetailsService userService;
//
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth
//				.userDetailsService(userService)
//				.passwordEncoder(new BCryptPasswordEncoder());
//	}
//
//	@Configuration
//	@Order(1)
//	@PropertySource({ "classpath:test-security.properties" })
//	public static class ApiTokenAuthenticationConfig extends WebSecurityConfigurerAdapter {
//
//		@Autowired private UserDetailsService userService;
//		@Autowired private Environment env;
//
//		@Bean
//		public TokenOperations tokenUtils(){
//			return new BasicTokenUtils(env.getRequiredProperty("test.token.key"));
//		}
//
//		@Bean
//		public AuthenticationTokenProcessingFilter authenticationTokenProcessingFilter(){
//			return new AuthenticationTokenProcessingFilter(tokenUtils(), userService);
//		}
//
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//			http
//					.sessionManagement()
//						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//					.and()
//					.addFilterBefore(authenticationTokenProcessingFilter(),
//							UsernamePasswordAuthenticationFilter.class)
//					.antMatcher(env.getRequiredProperty("test.secured.url"))
//						.authorizeRequests()
//							.antMatchers(HttpMethod.GET, env.getRequiredProperty("test.secured.url")).fullyAuthenticated()
//							.antMatchers(HttpMethod.POST, env.getRequiredProperty("test.secured.url")).fullyAuthenticated()
//							.antMatchers(HttpMethod.PUT, env.getRequiredProperty("test.secured.url")).fullyAuthenticated()
//							.antMatchers(HttpMethod.DELETE, env.getRequiredProperty("test.secured.url")).fullyAuthenticated()
//							.antMatchers(HttpMethod.PATCH, env.getRequiredProperty("test.secured.url")).fullyAuthenticated()
//							.antMatchers(HttpMethod.OPTIONS, env.getRequiredProperty("test.secured.url")).permitAll()
//							.antMatchers(HttpMethod.HEAD, env.getRequiredProperty("test.secured.url")).permitAll()
//					.and()
//					.csrf().disable();
//		}
//	}
//
//	@Configuration
//	@Order(2)
//	public static class BasicWebSecurtiyConfig extends WebSecurityConfigurerAdapter {
//
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//			http
//					.authorizeRequests()
//					.anyRequest().authenticated()
//					.and().sessionManagement()
//					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//					.and().httpBasic()
//					.and().csrf().disable();
//		}
//	}
//	
//}
