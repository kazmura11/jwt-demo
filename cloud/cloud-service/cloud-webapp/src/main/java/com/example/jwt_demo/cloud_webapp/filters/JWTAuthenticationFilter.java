package com.example.jwt_demo.cloud_webapp.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.jwt_demo.cloud_webapp.consts.SecurityConstants;
import com.example.jwt_demo.cloud_webapp.forms.UserForm;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * API認証フィルター
 *
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.authenticationManager = authenticationManager;

		// ログイン用のpathを変更する
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(SecurityConstants.LOGIN_URL, "POST"));

		// ログイン用のID/PWのパラメータ名を変更する
		setUsernameParameter(SecurityConstants.LOGIN_ID);
		setPasswordParameter(SecurityConstants.PASSWORD);
	}

	// 認証の処理
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
			HttpServletResponse res) throws AuthenticationException {
		try {
			// requestパラメータからユーザー情報を読み取る
			UserForm userForm = new ObjectMapper().readValue(req.getInputStream(), UserForm.class);

			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							userForm.getEmailAddress(),
							userForm.getPassword(),
							new ArrayList<>()));
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	// 認証に成功した場合の処理
	@Override
	protected void successfulAuthentication(HttpServletRequest req,
			HttpServletResponse res,
			FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		byte[] keyBytes = SecurityConstants.SECRET.getBytes();
		SecretKey key = Keys.hmacShaKeyFor(keyBytes);
		String token = Jwts.builder()
				.setSubject(((User) auth.getPrincipal()).getUsername()) // usernameだけを設定する
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(key, SignatureAlgorithm.HS512)
				.compact();
		res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
	}

}
