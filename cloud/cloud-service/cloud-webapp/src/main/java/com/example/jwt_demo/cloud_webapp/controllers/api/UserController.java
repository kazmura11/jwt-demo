package com.example.jwt_demo.cloud_webapp.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwt_demo.common.spring.models.entities.User;
import com.example.jwt_demo.common.spring.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	UserService userService;

	/*
	@GetMapping
	public ResponseEntity<User> getUser(@AuthenticationPrincipal LoginUserDetails userDetail) {
		HttpStatus status = HttpStatus.OK;
		HttpHeaders headers = new HttpHeaders();
		log.info("get login user");
		log.info(userDetail.getUser().toString());
		Optional<User> result = userService.getUser(userDetail.getUser().getEmailAddress());
		if (!result.isPresent()) {
			log.info("error");
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			headers.add("X-ERROR-RESPONSE", "user not found");
		}
		return new ResponseEntity<>(result.get(), headers, status);
	}
	*/
	
	@GetMapping("all")
	ResponseEntity<List<User>> getUsers() {
		return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
	}
}
