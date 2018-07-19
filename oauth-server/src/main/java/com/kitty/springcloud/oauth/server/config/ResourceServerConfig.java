package com.kitty.springcloud.oauth.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
	 * 
	 * @ClassName: ResourceServerConfig
	 * @Description: 资源服务器
	 * @author: Kitty
	 * @date: 2018年7月7日 下午2:17:52
	 *
	 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final String AUTH_RESOURCE_ID = "*";

//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/health");
//	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// 用表单登录
		http.formLogin()
				// 对请求授权
				.and().authorizeRequests()
				.antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
						"/swagger-ui.html", "/webjars/**")
				.permitAll()
				// 所有需要restful保护的资源都需要加入到这个requestMatchers，加入到的资源作为资源服务器保护的资源
				.and().requestMatchers().antMatchers("/users", "/**/users").and().authorizeRequests()
				.antMatchers("/**/users", "/users").authenticated().anyRequest().authenticated() // 所有的请求认证
				.and().csrf().disable() // 关闭Could not verify the provided
										// CSRF
										// token because your session was
										// not
										// found
		;
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(AUTH_RESOURCE_ID).stateless(true);
	}

}
