package com.kitty.springcloud.oauth.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;

import com.kitty.springcloud.oauth.server.security.RedisTemplateTokenStore;

/**
 * 
 * @ClassName: AuthorizationServerConfig
 * @Description: oauth2.0服务相关配置处理
 * @author: Kitty
 * @date: 2018年7月7日 下午2:42:50
 *
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private RedisConnectionFactory connectionFactory;

	@Autowired
	@Qualifier("userDetailServiceImpl")
	private UserDetailsService userDetailsService;

	@Autowired
	@Qualifier("clientDetailsServiceImpl")
	private ClientDetailsService clientDetailsService;
	
	/**
	 * redis集群操作类redisTemplate初始化
	 * @param connectionFactory
	 * @return
	 */
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(connectionFactory);
		template.setValueSerializer(new JdkSerializationRedisSerializer());
		// 使用StringRedisSerializer来序列化和反序列化redis的key值
		template.setKeySerializer(new StringRedisSerializer());
		template.afterPropertiesSet();
		return template;
	}
	
	/**
	 * redis缓存token处理逻辑
	 * @return
	 */
	@Bean
	public RedisTemplateTokenStore tokenStore() {
		// return new RedisTokenStore(connectionFactory);
		RedisTemplateTokenStore redisTemplateStore = new RedisTemplateTokenStore();
		redisTemplateStore.setRedisTemplate(redisTemplate(connectionFactory));
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
