package com.asif.manageurexpense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ManageurexpenseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManageurexpenseApplication.class, args);
	}

}
