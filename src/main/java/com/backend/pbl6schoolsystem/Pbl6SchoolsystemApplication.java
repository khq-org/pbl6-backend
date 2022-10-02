package com.backend.pbl6schoolsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class Pbl6SchoolsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(Pbl6SchoolsystemApplication.class, args);
	}

}
