package org.oncoblocks.centromere.core.test.config;

import org.oncoblocks.centromere.core.web.security.AuthenticationTokenProcessingFilter;
import org.oncoblocks.centromere.core.web.security.BasicTokenUtils;
import org.oncoblocks.centromere.core.web.security.TokenOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
 * @author woemler
 */

@Configuration
@EnableWebSecurity
@EnableWebMvcSecurity
public class TestSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired private UserDetailsService userService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.userDetailsService(userService)
				.passwordEncoder(new BCryptPasswordEncoder());
	}

	@Configuration
	@Order(1)
	@PropertySource({ "classpath:test-security.properties" })
	public static class ApiTokenAuthenticationConfig extends WebSecurityConfigurerAdapter {

		@Autowired private UserDetailsService userService;
		@Autowired private Environment env;

		@Bean
		public TokenOperations tokenUtils(){
			return new BasicTokenUtils(env.getRequiredProperty("token.key"));
		}

		@Bean
		public AuthenticationTokenProcessingFilter authenticationTokenProcessingFilter(){
			return new AuthenticationTokenProcessingFilter(tokenUtils(), userService);
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
					.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					.and()
					.addFilterBefore(authenticationTokenProcessingFilter(),
							UsernamePasswordAuthenticationFilter.class)
					.antMatcher(env.getRequiredProperty("api.url"))
					.authorizeRequests()
					.antMatchers(HttpMethod.GET, env.getRequiredProperty("api.url")).fullyAuthenticated()
					.antMatchers(HttpMethod.POST, env.getRequiredProperty("api.url")).fullyAuthenticated()
					.antMatchers(HttpMethod.PUT, env.getRequiredProperty("api.url")).fullyAuthenticated()
					.antMatchers(HttpMethod.DELETE, env.getRequiredProperty("api.url")).fullyAuthenticated()
					.antMatchers(HttpMethod.PATCH, env.getRequiredProperty("api.url")).fullyAuthenticated()
					.antMatchers(HttpMethod.OPTIONS, env.getRequiredProperty("api.url")).permitAll()
					.antMatchers(HttpMethod.HEAD, env.getRequiredProperty("api.url")).permitAll()
					.and()
					.csrf().disable();
		}
	}

	@Configuration
	@Order(2)
	public static class BasicWebSecurtiyConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
					.authorizeRequests()
					.anyRequest().authenticated()
					.and().sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					.and().httpBasic()
					.and().csrf().disable();
		}
	}
	
}
