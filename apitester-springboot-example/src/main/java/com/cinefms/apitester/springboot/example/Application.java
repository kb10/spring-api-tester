package com.cinefms.apitester.springboot.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages="com")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
