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

package org.oncoblocks.centromere.core.test.web.security;

import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.oncoblocks.centromere.core.test.config.TestMongoConfig;
import org.oncoblocks.centromere.core.test.config.TestSecurityConfig;
import org.oncoblocks.centromere.core.test.config.TestWebConfig;
import org.oncoblocks.centromere.core.test.repository.mongo.EntrezGeneRepository;
import org.oncoblocks.centromere.core.test.repository.mongo.MongoRepositoryConfig;
import org.oncoblocks.centromere.core.test.repository.mongo.UserRepository;
import org.oncoblocks.centromere.core.test.web.controller.crud.CrudControllerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.jayway.jsonassert.impl.matcher.IsMapContainingKey.hasKey;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author woemler
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestMongoConfig.class, TestWebConfig.class, CrudControllerConfig.class,
		TestSecurityConfig.class, SecurityContextConfig.class, MongoRepositoryConfig.class})
@WebAppConfiguration
@FixMethodOrder
public class SecurityTests {

	private MockMvc mockMvc;
	@Autowired private WebApplicationContext webApplicationContext;
	@Autowired UserRepository userRepository;

	@Autowired
	@SuppressWarnings("SpringJavaAutowiringInspection")
	private FilterChainProxy springSecurityFilterChain;

	@Autowired private EntrezGeneRepository geneRepository;
	
	private static boolean isConfigured = false;

	@Before
	public void setup(){

		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilter(springSecurityFilterChain)
				.build();
		
		if (isConfigured) return;
		
		userRepository.deleteAll();

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		Set<String> roles = new HashSet<>();
		roles.add("USER");
		User user = new User();
		user.setName("User");
		user.setUsername("user");
		user.setPassword(encoder.encode("user"));
		user.setEmail("user@email.com");
		user.setRegistrationDate(new Date());
		user.setRoles(roles);

		userRepository.insert(user);
		
		roles.add("ADMIN");

		User admin = new User();
		admin.setName("Admin");
		admin.setUsername("admin");
		admin.setPassword(encoder.encode("admin"));
		admin.setEmail("admin@email.com");
		admin.setRegistrationDate(new Date());
		admin.setRoles(roles);

		userRepository.insert(admin);

		isConfigured = true;

	}

	@Test
	public void testSecuredUrl() throws Exception {
		mockMvc.perform(get("/secured/genes"))
				.andExpect(status().isForbidden());
	}

	@Test
	public void testAuthenticationBadCredentials() throws Exception {

		mockMvc.perform(post("/authenticate")
				.with(httpBasic("bad", "creds")))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void testAuthenticationGoodCredentials() throws Exception {

		mockMvc.perform(post("/authenticate")
				.with(httpBasic("user", "user")))
				.andExpect(status().isOk());
	}

	@Test
	public void testTokenAuthentication() throws Exception {

		MvcResult result = mockMvc.perform(post("/authenticate")
				.with(httpBasic("user", "user")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasKey("token")))
				.andReturn();

		String json = result.getResponse().getContentAsString();
		String token = JsonPath.read(json, "$.token");

		mockMvc.perform(get("/secured/genes")
				.header("X-Auth-Token", token))
				.andExpect(status().isOk());

	}



}
