package com.kitty.springcloud.oauth.server.config;

import javax.servlet.http.HttpServletResponse;

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

	@Override
	public void configure(HttpSecurity http) throws Exception {
		   http
           .csrf().disable()
           .exceptionHandling()
           .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
           .and()
           .authorizeRequests().antMatchers("/login").permitAll()
           .anyRequest().authenticated()
           .and()
           .httpBasic();
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(AUTH_RESOURCE_ID).stateless(true);
	}
	
	
}
