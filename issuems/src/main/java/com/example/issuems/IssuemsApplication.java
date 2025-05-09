package com.example.issuems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController

public class IssuemsApplication {

	public static void main(String[] args) {
		SpringApplication.run(IssuemsApplication.class, args);
	}

}
