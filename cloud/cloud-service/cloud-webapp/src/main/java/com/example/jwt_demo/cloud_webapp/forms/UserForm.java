package com.example.jwt_demo.cloud_webapp.forms;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

/**
 * JWTAuthenticationFilter
 * 認証で使うフォーム
 *
 */
@Data
public class UserForm {
	private String emailAddress;

	private String password;

	public void encrypt(PasswordEncoder encoder) {
		this.password = new BCryptPasswordEncoder().encode(password);
	}
}
