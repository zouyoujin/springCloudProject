package com.kitty.springcloud.oauth.server.config;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.kitty.springcloud.oauth.server.security.ClientDetailsServiceImpl;
import com.kitty.springcloud.oauth.server.security.RedisTemplateTokenStore;

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

	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(connectionFactory);
		template.setValueSerializer(new JdkSerializationRedisSerializer());
		// 使用StringRedisSerializer来序列化和反序列化redis的key值
		template.setKeySerializer(new StringRedisSerializer());
		template.afterPropertiesSet();
		return template;
	}

	@Bean
	public RedisTemplateTokenStore tokenStore() {
		// return new RedisTokenStore(connectionFactory);
		RedisTemplateTokenStore redisTemplateStore = new RedisTemplateTokenStore();
		redisTemplateStore.setRedisTemplate(redisTemplate(connectionFactory));
		return redisTemplateStore;
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

		endpoints.authenticationManager(authenticationManager).userDetailsService(userDetailsService)
				// .tokenStore(new InMemoryTokenStore());
				.tokenStore(tokenStore());// redis保存token

		// 配置TokenServices参数
		// DefaultTokenServices tokenServices = new DefaultTokenServices();
		// tokenServices.setTokenStore(endpoints.getTokenStore());
		// tokenServices.setSupportRefreshToken(false);
		// tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
		// tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
		// tokenServices.setAccessTokenValiditySeconds((int)
		// TimeUnit.DAYS.toSeconds(30)); // 30天
		// endpoints.tokenServices(tokenServices);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")
				.allowFormAuthenticationForClients(); // 允许表单认证
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// 配置客户端, 用于client认证
		//clients.withClientDetails(clientDetails);
		 //使用存在内存中配置
		 clients.inMemory() // 使用in-memory存储
		 .withClient("webapp") // client_id
		 .secret("webapp") // client_secret
		 .authorizedGrantTypes("password","refresh_token") // 该client允许的授权类型
		 .scopes("read", "write")     // 允许的授权范围
		 .accessTokenValiditySeconds(10000) //token过期时间
         .refreshTokenValiditySeconds(10000); //refresh过期时间
	}
}
