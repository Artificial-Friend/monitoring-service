package com.ua.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@SpringBootApplication
@EnableEurekaClient
@EnableWebFluxSecurity
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class);
	}
}
