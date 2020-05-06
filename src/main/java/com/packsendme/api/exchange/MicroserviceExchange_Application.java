package com.packsendme.api.exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MicroserviceExchange_Application {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceExchange_Application.class, args);
	}
}

