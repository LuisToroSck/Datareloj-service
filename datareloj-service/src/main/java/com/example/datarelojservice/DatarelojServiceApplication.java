package com.example.datarelojservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DatarelojServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatarelojServiceApplication.class, args);
	}

}
