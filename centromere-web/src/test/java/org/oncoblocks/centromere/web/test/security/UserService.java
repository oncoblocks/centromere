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

package org.oncoblocks.centromere.web.test.security;

import org.oncoblocks.centromere.core.repository.QueryCriteria;
import org.oncoblocks.centromere.web.test.repository.mongo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author woemler
 */

@Service
public class UserService implements UserDetailsService {

	@Autowired UserRepository userRepository;
	
	@Override 
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<QueryCriteria> criterias = new ArrayList<>();
		criterias.add(new QueryCriteria("username", username));
		List<User> users = userRepository.find(criterias);
		return users.get(0);
	}
}
