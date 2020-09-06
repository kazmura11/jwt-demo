package com.example.jwt_demo.cloud_webapp.controllers.admin.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.jwt_demo.cloud_webapp.consts.SecurityConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(SecurityConstants.ADMIN_LOGIN_URL)
public class LoginController {
	
	@GetMapping
	public String loginForm() {
		return SecurityConstants.ADMIN_LOGIN_URL;
	}

	@GetMapping(path = "/success")
	public void loginPageRedirect(HttpServletResponse response, Authentication authResult) throws ServletException, IOException {
		String role =  authResult.getAuthorities().toString();
		log.info("ROLE : {}", role);
		if (role.contains("ROLE_ADMIN")) {
			response.sendRedirect("/admin/master");
		} else {
			response.sendRedirect(SecurityConstants.ADMIN_LOGIN_URL + "?error");
		}
	}

}
