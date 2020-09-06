package com.example.jwt_demo.cloud_webapp.controllers.admin.master;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;;

@Slf4j
@Controller
@RequestMapping("/admin/master")
public class IndexController {
	@GetMapping
	public String index() {
		log.debug("/admin/master/index.html");
		return "/admin/master/index";
	}
}
