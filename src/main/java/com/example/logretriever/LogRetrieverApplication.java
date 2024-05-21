package com.example.logretriever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class LogRetrieverApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogRetrieverApplication.class, args);
	}
}
