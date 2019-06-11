package com.kitty.springcloud.api.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 
 * @ClassName: TokenFilter   
 * @Description:token过滤器处理，拦截请求参数,如果没有带token参数请求失败
 * @author: Kitty
 * @date: 2019年6月11日 下午11:34:21   
 *
 */
@Slf4j
public class TokenFilter implements GlobalFilter, Ordered {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String token = exchange.getRequest().getQueryParams().getFirst("token");
		if (token == null || token.isEmpty()) {
			log.info("token is empty...");
//			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//			return exchange.getResponse().setComplete();
		}
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return -100;
	}
}
