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

package org.oncoblocks.centromere.web.test.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.web.security.BasicTokenUtils;
import org.oncoblocks.centromere.web.security.TokenOperations;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * @author woemler
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TokenTests.TokenTestConfig.class})
public class TokenTests {
	
	private User user;
	private TokenOperations tokenOperations = new BasicTokenUtils("test");
	
	@Before
	public void setup(){
		user = new User();
		user.setUsername("user");
		user.setPassword("password");
		user.setEmail("user@email.com");
		user.setName("Test User");
		user.setRegistrationDate(new Date());
	}
	
	@Test
	public void tokenGeneration(){
		String token = tokenOperations.createToken(user);
		Assert.notNull(token);
		Assert.isTrue(!token.equals(""));
		Assert.isTrue(token.startsWith("user"));
	}
	
	@Test
	public void getUsernameFromToken(){
		String token = tokenOperations.createToken(user);
		String username = tokenOperations.getUserNameFromToken(token);
		Assert.notNull(username);
		Assert.isTrue("user".equals(username));
	}
	
	@Test
	public void validateToken(){
		String token = tokenOperations.createToken(user);
		Assert.isTrue(tokenOperations.validateToken(token, user));
		token = "user:345983520:tnu49ng4ug4tgbu4gug4u";
		Assert.isTrue(!tokenOperations.validateToken(token, user));
	}
	
	@Configuration
	public static class TokenTestConfig {}
	
}
