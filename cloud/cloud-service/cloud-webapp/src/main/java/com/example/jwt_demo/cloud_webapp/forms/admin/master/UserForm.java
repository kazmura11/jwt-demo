package com.example.jwt_demo.cloud_webapp.forms.admin.master;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class UserForm {

	@NotBlank
	private String emailAddress;

	@NotBlank
	private String name;

	@NotBlank
	private String password;

	private boolean adminFlag;

	private boolean deleted;
}
