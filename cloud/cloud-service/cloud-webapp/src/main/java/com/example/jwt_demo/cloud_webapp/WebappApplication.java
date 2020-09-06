package com.example.jwt_demo.cloud_webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.jwt_demo.cloud_webapp.generators.FQCNBeanNameGenerator;

@SpringBootApplication
@ComponentScan(basePackages = { "com.example.jwt_demo.common", "com.example.jwt_demo.cloud_webapp" }, nameGenerator = FQCNBeanNameGenerator.class)
@EntityScan(basePackages = { "com.example.jwt_demo.common.spring.models.entities", "com.example.jwt_demo.cloud_webapp" })
@EnableJpaRepositories(basePackages = { "com.example.jwt_demo.common.spring.repositories", "com.example.jwt_demo.cloud_webapp" })
public class WebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebappApplication.class, args);
	}

}
