package com.kitty.springcloud.oauth.server.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitty.springcloud.oauth.server.security.ClientDetailsServiceImpl;
import com.kitty.springcloud.oauth.server.security.RedisTemplateTokenStore;

/**
 * 
 * @ClassName: AuthorizationServerConfig
 * @Description: oauth2.0服务相关配置处理
 * @author: Kitty
 * @date: 2018年7月7日 下午2:42:50
 *
 */
@Component
@Configuration
@EnableAuthorizationServer
@AutoConfigureAfter(AuthorizationServerEndpointsConfigurer.class)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	/**
	 * 注入authenticationManager 来支持 password grant type
	 */
	@Autowired
	private AuthenticationManager authenticationManager;

	@Resource
	private ObjectMapper objectMapper; // springmvc启动时自动装配json处理类

	@Autowired
	@Qualifier("userDetailServiceImpl")
	private UserDetailsService userDetailsService;

	@Autowired
	@Qualifier("clientDetailsServiceImpl")
	private ClientDetailsServiceImpl clientDetailsService;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * redis缓存token处理逻辑
	 * 
	 * @return
	 */
	@Bean
	public RedisTemplateTokenStore tokenStore() {
		// return new RedisTokenStore(connectionFactory);
		RedisTemplateTokenStore redisTemplateStore = new RedisTemplateTokenStore();
		redisTemplateStore.setRedisTemplate(redisTemplate);
		return redisTemplateStore;
	}

	/**
	 * 权限验证相关配置处理
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager).userDetailsService(userDetailsService)
				.tokenStore(tokenStore());// redis保存token
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")
				.allowFormAuthenticationForClients(); // 允许表单认证
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// 配置客户端, 用于client认证
		clients.withClientDetails(clientDetailsService);
		// 加载客户端数据到缓存
		clientDetailsService.loadAllClientToCache();
		// 使用存在内存中配置
		// clients.inMemory() // 使用in-memory存储
		// .withClient("webapp") // client_id
		// .secret("webapp") // client_secret
		// .authorizedGrantTypes("authorization_code","password","refresh_token")
		// // 该client允许的授权类型
		// .scopes("read", "write") // 允许的授权范围
		// .accessTokenValiditySeconds(10000) //token过期时间
		// .refreshTokenValiditySeconds(10000); //refresh过期时间
	}
}
