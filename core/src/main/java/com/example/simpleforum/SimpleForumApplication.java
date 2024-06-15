package com.example.simpleforum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class SimpleForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleForumApplication.class, args);
	}
}
