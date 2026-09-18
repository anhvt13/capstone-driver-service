package com.driver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DriverApplication {
	public static void main(String[] args) {
        SpringApplication.run(DriverApplication.class, args);
	}

}
