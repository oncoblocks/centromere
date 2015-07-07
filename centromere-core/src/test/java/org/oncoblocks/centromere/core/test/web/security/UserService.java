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
