package com.kitty.springcloud.oauth.client;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

import com.kitty.springcloud.oauth.filter.TokenFilter;

@Configuration
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
			http.formLogin().and().authorizeRequests().antMatchers("/", "/anon", "/login")
				.permitAll().and()
				// 这里之所以要禁用csrf，是为了方便。
				// 否则，退出链接必须要发送一个post请求，请求还得带csrf token
				// 那样我还得写一个界面，发送post请求
				.antMatcher("/**")
				// 所有请求都得经过认证和授权
				.authorizeRequests().anyRequest().authenticated().and()
				.csrf().disable()
				// 退出的URL是/logout
				.logout().logoutUrl("/logout").permitAll()
				// 退出成功后，跳转到/路径。
				.logoutSuccessUrl("/login");

		// 新增token过滤器
	}
	
	   @Bean
			public FilterRegistrationBean mytokenFilter(  SecurityProperties security ,AuthorizationCodeResourceDetails authorizationCodeResourceDetails ,ResourceServerProperties resourceServerProperties) {
				FilterRegistrationBean registration = new FilterRegistrationBean();
				
				TokenFilter tokenFilter = new TokenFilter();
				registration.setFilter(tokenFilter);
				registration.setOrder(security.getFilterOrder() - 11);
				
//				authorizationCodeResourceDetails.setUserAuthorizationUri("http://127.0.0.1:8000/auth/oauth/authorize");
//				authorizationCodeResourceDetails.setAccessTokenUri("http://127.0.0.1:8000/auth/oauth/token");  //http://127.0.0.1:8000/auth/oauth/authorize
//				
//				resourceServerProperties.setTokenInfoUri("http://127.0.0.1:8000/auth/oauth/check_token");
					
				return registration;
			}
}
