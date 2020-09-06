package com.example.jwt_demo.cloud_webapp.models.entities;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.example.jwt_demo.common.spring.models.entities.User;

public class LoginUserDetails extends org.springframework.security.core.userdetails.User {
	private final User user;

	public LoginUserDetails(User user, List<GrantedAuthority> grantedAuthorities) {
		super(user.getEmailAddress(), user.getPassword(), grantedAuthorities);
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}

}
