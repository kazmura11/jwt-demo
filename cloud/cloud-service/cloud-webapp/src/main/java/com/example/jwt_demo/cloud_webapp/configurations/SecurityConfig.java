package com.example.jwt_demo.cloud_webapp.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.jwt_demo.cloud_webapp.consts.SecurityConstants;
import com.example.jwt_demo.cloud_webapp.filters.JWTAuthenticationFilter;
import com.example.jwt_demo.cloud_webapp.filters.JWTAuthorizationFilter;

@EnableWebSecurity
public class SecurityConfig {

	// JWTによるログイン設定
	@Configuration
	@Order(1)
	public static class BatchConfiguereAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		private UserDetailsService userDetailsService;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
					.antMatcher("/api/**")
						.cors()
						.and()
					.authorizeRequests()
						.antMatchers("/public/", SecurityConstants.SIGNUP_URL, SecurityConstants.LOGIN_URL).permitAll()
						.anyRequest().authenticated()
					.and()
						.logout()
					.and().csrf().disable()
						.addFilter(new JWTAuthenticationFilter(authenticationManager(), bCryptPasswordEncoder()))
						.addFilter(new JWTAuthorizationFilter(authenticationManager()))
						.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		}

		@Autowired
		public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
			auth
					.userDetailsService(userDetailsService)
					.passwordEncoder(bCryptPasswordEncoder());
		}

		@Bean
		public BCryptPasswordEncoder bCryptPasswordEncoder() {
			return new BCryptPasswordEncoder();
		}
	}
	
	@Configuration
	@Order(2)
	public static class AdminConfiguereAdapter extends WebSecurityConfigurerAdapter {
		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/css/**", "/vendor/**");
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
					.antMatcher("/admin/**")
						.authorizeRequests()
						.antMatchers(
								SecurityConstants.ADMIN_LOGIN_URL,
								SecurityConstants.ADMIN_LOGIN_SUCCESS_URL
						).permitAll()
						.antMatchers("/admin/**").hasRole("ADMIN")
						.anyRequest().authenticated()
					.and()
						.formLogin()
						.loginProcessingUrl(SecurityConstants.ADMIN_LOGIN_PROCESSING_URL)
						.loginPage(SecurityConstants.ADMIN_LOGIN_URL)
						.failureUrl(SecurityConstants.ADMIN_LOGIN_URL + "?error")
						.defaultSuccessUrl(SecurityConstants.ADMIN_LOGIN_SUCCESS_URL, true)
						.usernameParameter("emailAddress").passwordParameter("password")
					.and()
						.logout()
						.logoutRequestMatcher(new AntPathRequestMatcher(SecurityConstants.ADMIN_LOGOUT_URL))
						.logoutSuccessUrl(SecurityConstants.ADMIN_LOGIN_URL)
						.deleteCookies("JSESSIONID")
						.invalidateHttpSession(true)
					.and()
						.csrf()
						.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		}

		@Bean
		PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
	}

}
