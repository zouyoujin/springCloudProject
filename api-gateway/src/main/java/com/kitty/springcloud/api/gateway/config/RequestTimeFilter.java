package com.kitty.springcloud.api.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class RequestTimeFilter implements GatewayFilter, Ordered {
	private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());
		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);
			if (startTime != null) {
				log.info(exchange.getRequest().getURI().getRawPath() + ": " + (System.currentTimeMillis() - startTime)
						+ "ms");
			}
		}));
	}

}
