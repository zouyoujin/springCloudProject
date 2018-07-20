package com.kitty.springcloud.oauth.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
//在默认情况下只能扫描与控制器在同一个包下以及其子包下的@Component注解，所以公共模块创建的一些bean也需要初始化
@ComponentScan(basePackages = { "com.kitty.springcloud.common", "com.kitty.springcloud.oauth.server" })
@MapperScan("com.kitty.springcloud.oauth.server.mapper*")
public class AuthServerApplication extends SpringBootServletInitializer {

	// @Bean(name = "auditorAware")
	// public AuditorAware<String> auditorAware() {
	// return () -> SecurityUtils.getCurrentUserUsername();
	// }

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}

	// @Override
	// public void addViewControllers(ViewControllerRegistry registry) {
	// registry.addViewController("/login").setViewName("login");
	// }
}
