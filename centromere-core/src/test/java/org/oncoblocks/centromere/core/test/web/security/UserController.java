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

import org.oncoblocks.centromere.core.web.security.ApiUserToken;
import org.oncoblocks.centromere.core.web.security.TokenOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;

/**
 * @author woemler
 */

@Controller
@RequestMapping(value = "/authenticate")
public class UserController {

	@Autowired TokenOperations tokenUtils;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public @ResponseBody ApiUserToken createToken(@AuthenticationPrincipal User user){

		Assert.notNull(user);
		String token = tokenUtils.createToken(user);
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		calendar.add(Calendar.HOUR, 1);
		Date expires = calendar.getTime();
		ApiUserToken apiUserToken = new ApiUserToken(token, user.getId(), now, expires);
		return apiUserToken;

	}
	
}
