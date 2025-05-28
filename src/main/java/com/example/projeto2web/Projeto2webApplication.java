package com.example.projeto2web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.projeto2.models")
@EnableJpaRepositories(basePackages = "com.example.projeto2.repositories")
@ComponentScan(basePackages = {"com.example.projeto2web", "com.example.projeto2.services"})
public class Projeto2webApplication {

	public static void main(String[] args) {
		SpringApplication.run(Projeto2webApplication.class, args);
	}

}
