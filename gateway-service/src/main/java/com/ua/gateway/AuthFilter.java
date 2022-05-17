package com.ua.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

	@Override
	public GatewayFilter apply(Config config) {
		return null;
	}

	public static class Config {

	}
}
