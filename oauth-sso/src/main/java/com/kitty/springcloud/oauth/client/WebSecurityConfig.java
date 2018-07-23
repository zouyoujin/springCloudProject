package com.kitty.springcloud.oauth.client;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.kitty.springcloud.common.utils.JsonUtils;
import com.kitty.springcloud.oauth.filter.TokenFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableOAuth2Sso
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.formLogin().and().authorizeRequests().antMatchers("/", "/anon", "/login").permitAll().and()
				// 这里之所以要禁用csrf，是为了方便。
				// 否则，退出链接必须要发送一个post请求，请求还得带csrf token
				// 那样我还得写一个界面，发送post请求
				.antMatcher("/**")
				// 所有请求都得经过认证和授权
				.authorizeRequests().anyRequest().authenticated().and().csrf().disable()
				// 退出的URL是/logout
				.logout().logoutUrl("/logout").permitAll()
				// 退出成功后，跳转到/路径。
				.logoutSuccessUrl("http://authserver:9999/user/logout");
//				.clearAuthentication(true).logoutSuccessHandler(new LogoutSuccessHandler() {
//					@Override
//					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
//							throws IOException, ServletException {
//						if (authentication != null) {
//							if (authentication instanceof OAuth2Authentication) {
//								log.info("onLogoutSuccess authentication user = " + JsonUtils.toJson(authentication));
//								OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
//								//TODO 退出系统成功后,oauth认证系统需要注销账号
//								//response.sendRedirect("http://authserver:9999/user/logout?access_token=" + details.getTokenValue());
//								// 重新发起sso登录 OAuth2ClientContextFilter redirectUser(redirect,
//								// request, response);
//							}
//						}
//					}
//				});
		// 新增token过滤器
	}

	@Bean
	public FilterRegistrationBean mytokenFilter(SecurityProperties security,
			AuthorizationCodeResourceDetails authorizationCodeResourceDetails,
			ResourceServerProperties resourceServerProperties) {
		FilterRegistrationBean registration = new FilterRegistrationBean();

		TokenFilter tokenFilter = new TokenFilter();
		registration.setFilter(tokenFilter);
		//链接器的顺序要放后面，否则获取不到鉴权的用户信息
		registration.setOrder(Integer.MAX_VALUE - 11);

		// authorizationCodeResourceDetails.setUserAuthorizationUri("http://127.0.0.1:8000/auth/oauth/authorize");
		// authorizationCodeResourceDetails.setAccessTokenUri("http://127.0.0.1:8000/auth/oauth/token");
		// //http://127.0.0.1:8000/auth/oauth/authorize
		//
		// resourceServerProperties.setTokenInfoUri("http://127.0.0.1:8000/auth/oauth/check_token");

		return registration;
	}
}
