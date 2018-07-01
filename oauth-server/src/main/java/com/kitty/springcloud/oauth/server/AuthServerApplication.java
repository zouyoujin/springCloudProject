package com.kitty.springcloud.oauth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableDiscoveryClient
@SpringBootApplication
// @ComponentScan("com.kitty.springcloud.oauth.server")
public class AuthServerApplication extends WebMvcConfigurerAdapter {

	// @Bean(name = "auditorAware")
	// public AuditorAware<String> auditorAware() {
	// return () -> SecurityUtils.getCurrentUserUsername();
	// }

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/oauth/confirm_access").setViewName("authorize");
	}

}
