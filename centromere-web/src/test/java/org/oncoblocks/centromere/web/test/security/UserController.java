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

import org.oncoblocks.centromere.web.security.TokenDetails;
import org.oncoblocks.centromere.web.security.TokenOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
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

	@Autowired private TokenOperations tokenUtils;
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public @ResponseBody TokenDetails createToken(@AuthenticationPrincipal User user){
		String token = tokenUtils.createToken(user);
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		calendar.add(Calendar.HOUR, 1);
		Date expires = calendar.getTime();
		return new TokenDetails(token, user.getId(), now, expires);
	}
	
}
