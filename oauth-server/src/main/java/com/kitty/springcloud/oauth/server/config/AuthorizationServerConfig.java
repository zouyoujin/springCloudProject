package com.kitty.springcloud.oauth.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import com.kitty.springcloud.oauth.server.security.ClientDetailsServiceImpl;

/**
 * Created by wangyunfei on 2017/6/9.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private RedisConnectionFactory connectionFactory;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private ClientDetailsServiceImpl clientDetails;

	@Bean
	public RedisTokenStore tokenStore() {
		return new RedisTokenStore(connectionFactory);
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager).userDetailsService(userDetailsService)
		// .tokenStore(tokenStore());// redis保存token
		.tokenStore(new InMemoryTokenStore());
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")
				.allowFormAuthenticationForClients(); // 允许表单认证
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// 配置客户端, 用于client认证
		clients.withClientDetails(clientDetails);
		// 使用存在内存中配置
//		clients.inMemory() // 使用in-memory存储
//				.withClient("client") // client_id
//				.secret("secret") // client_secret
//				.authorizedGrantTypes("authorization_code") // 该client允许的授权类型
//				.scopes("app"); // 允许的授权范围
	}
}
