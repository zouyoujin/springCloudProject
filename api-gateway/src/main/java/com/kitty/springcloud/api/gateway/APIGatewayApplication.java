package com.kitty.springcloud.api.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import com.kitty.springcloud.api.gateway.config.RequestTimeFilter;
import com.kitty.springcloud.api.gateway.config.TokenFilter;

/**
 * 
 * @ClassName: GatewayApplication
 * @Description: Gate 网关
 * @author: Kitty
 * @date: 2018年7月29日 上午11:19:48
 *
 */
@EnableDiscoveryClient
@EnableEurekaClient
@SpringBootApplication
public class APIGatewayApplication {

	private static final Logger logger = LoggerFactory.getLogger(APIGatewayApplication.class);

	@Bean
	public TokenFilter tokenFilter() {
		return new TokenFilter();
	}
	
//	@Bean
//	public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
//		// @formatter:off
//		return builder.routes()
//				.route(r -> r.path("/**")
//						.filters(f -> f.filter(new RequestTimeFilter()).addResponseHeader("X-Response-Default-Foo",
//								"Default-Bar"))
//						.uri("http://192.168.140.190:9527").order(0).id("customer_filter_router"))
//				.build();
//		// @formatter:on
//	}

	public static void main(String[] args) {
		SpringApplication.run(APIGatewayApplication.class, args);
		logger.info(" Start APIGatewayApplication Done.");
	}
}
