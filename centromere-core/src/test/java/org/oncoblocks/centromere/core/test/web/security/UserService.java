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

import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.oncoblocks.centromere.core.test.repository.mongo.UserRepository;
import org.oncoblocks.centromere.core.web.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author woemler
 */

@Service
public class UserService extends GenericService<User, String> implements UserDetailsService {

	@Autowired
	public UserService(UserRepository repository) {
		super(repository);
	}

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		QueryCriteria queryCriteria = new QueryCriteria("username", username);
		List<User> users = (List<User>) repository.find(
				Arrays.asList(new QueryCriteria[] {queryCriteria}));
		if (users != null && users.size() > 0) {
			return users.get(0);
		} else {
			throw new UsernameNotFoundException(String.format("User %s not found!", username));
		}
	}
	
}
