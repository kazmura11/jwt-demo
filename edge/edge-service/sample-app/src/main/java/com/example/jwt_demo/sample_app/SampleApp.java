package com.example.jwt_demo.sample_app;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.jwt_demo.edge_common.util.AuthenticationUtil;
import com.example.jwt_demo.edge_common.util.HttpRequestUtil;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = { "com.example.jwt_demo.common", "com.example.jwt_demo.sample_app" })
@EntityScan(basePackages = { "com.example.jwt_demo.common.spring.models.entities", "com.example.jwt_demo.sample_app" })
@EnableJpaRepositories(basePackages = { "com.example.jwt_demo.common.spring.repositories", "com.example.jwt_demo.sample_app" })
public class SampleApp implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(SampleApp.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		String token = AuthenticationUtil.getInstace().getServerAccessToken("demo@example.com", "demo", "http://localhost:8080/api/login", null);
		String result = HttpRequestUtil.getInstace().get(token, "http://localhost:8080/api/user/all", null);
		System.out.println("result : " + result);
	}

}
