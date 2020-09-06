package com.example.jwt_demo.cloud_webapp.consts;

public class SecurityConstants {
	public static final String SECRET = "this_is_a_secret_key_aaaaaaaaaaaaaaaaa_which_requires_more_than_440_bits";
	public static final long EXPIRATION_TIME = 28_800_000; // 8 hours
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGNUP_URL = "/api/signup";
	public static final String LOGIN_URL = "/api/login";
	public static final String LOGIN_ID = "emailAddress"; // defalut:username
	public static final String PASSWORD = "password"; // default:password

	public static final String FRONTEND_LOGIN_URL = "/frontend/login";
	public static final String FRONTEND_LOGIN_FORM_URL = "/frontend/login/loginForm";
	public static final String FRONTEND_LOGIN_SUCCESS_URL = "/frontend/login/success";
	public static final String FRONTEND_LOGOUT_URL = "/frontend/logout";

	public static final String ADMIN_LOGIN_PROCESSING_URL = "/admin/login/loginProcess";
	public static final String ADMIN_LOGIN_URL = "/admin/login";
	public static final String ADMIN_LOGIN_SUCCESS_URL = "/admin/login/success";
	public static final String ADMIN_LOGOUT_URL = "/admin/logout";
}