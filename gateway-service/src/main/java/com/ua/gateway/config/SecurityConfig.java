package com.ua.gateway.config;

import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfig {

//	@Bean
//	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
//		http
//			.authorizeExchange()
//				.pathMatchers("/resource").hasAuthority("SCOPE_resource.read")
//				.anyExchange().authenticated()
//				.and()
//			.oauth2ResourceServer()
//				.jwt();
//		return http.build();
//	}

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		return http
				.authorizeExchange(exchange -> exchange.matchers(EndpointRequest.toAnyEndpoint()).permitAll()
						.anyExchange().authenticated())
				.oauth2Login(Customizer.withDefaults())
				.build();
	}
}
