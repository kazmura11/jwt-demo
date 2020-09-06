package com.example.jwt_demo.cloud_webapp.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.jwt_demo.cloud_webapp.models.entities.LoginUserDetails;
import com.example.jwt_demo.common.spring.models.entities.User;
import com.example.jwt_demo.common.spring.repositories.UserRepository;

@Service
public class LoginUserDetailsService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(LoginUserDetailsService.class);

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
		Optional<User> result = userRepository.findByEmailAddressEqualsAndDeletedIsFalse(emailAddress);
		if (!result.isPresent()) {
			throw new UsernameNotFoundException("The requested user is not found");
		}
		User user = result.get();
		List<GrantedAuthority> grantedAuthorities = null;
		if (user.isAdminFlag()) {
			grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN");
			logger.info("ADMIN LOGIN");
		} else {
			grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
			logger.info("USER LOGIN");
		}

		return new LoginUserDetails(user, grantedAuthorities);
	}
}