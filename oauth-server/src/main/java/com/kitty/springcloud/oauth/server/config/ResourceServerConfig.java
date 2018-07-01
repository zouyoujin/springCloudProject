package com.kitty.springcloud.oauth.server.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * Created by wangyunfei on 2017/6/9.
 */
@Configuration
@EnableResourceServer
@Order(6)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final String DEMO_RESOURCE_ID = "*";

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(DEMO_RESOURCE_ID).stateless(true);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().exceptionHandling()
				.authenticationEntryPoint(
						(request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
				.and().authorizeRequests().anyRequest().authenticated().and().httpBasic();

		/*http
			// 关闭csrf
			// .csrf().disable()
			// 取消安全报文头 默认开启 可配置部分开启或全关
			// .headers().disable()
			// 登录页面url 配置登录成功后调用的方法
			.formLogin().loginPage("/login").permitAll().successHandler(new LoginSuccessHandler())
			.and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
			.authorizeRequests()
			// 配置这些链接无需验证
			.antMatchers("/regist", "/toregist").permitAll()
			// 除以上路径都需要验证
			.anyRequest().authenticated()
			// 路径角色验证
			// .antMatchers("/admin/**").hasRole("ADMIN")
			// 排除该路径角色认证 注意顺序自上而下
			// .antMatchers("/**").hasRole("USER")
			.and()
			// 注销登录 默认支持 销毁session并且清空配置的rememberMe()认证 跳转登录页 或配置的注销成功页面
			.logout()
			.deleteCookies("remove")
			.invalidateHttpSession(false)
			.logoutUrl("/logout")
			.logoutSuccessUrl("/logoutsuccess")
			.and()
			// http参数中包含一个名为“remember-me”的参数，不管session是否过期，用户记录将会被记保存下来
			.rememberMe()
			// .and()
			//// http映射https
			// .portMapper()
			// .http(8080).mapsTo(9443)
			.and()
			// 配置http认证
			.httpBasic()
			.and()
			// 当用户进行重复登录时 强制销毁前一个登录用户 配置此应用必须添加Listener
			// HttpSessionEventPublisher
			.sessionManagement()
			.maximumSessions(1)
			.expiredUrl("/login?expired");*/
	}
}
