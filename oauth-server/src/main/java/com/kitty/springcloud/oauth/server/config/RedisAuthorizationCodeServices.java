package com.kitty.springcloud.oauth.server.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName: RedisAuthorizationCodeServices   
 * @Description:验证code类型逻辑处理,验证码存在Redis数据库==>JdbcAuthorizationCodeServices替换
 * @author: Kitty
 * @date: 2018年7月22日 上午2:05:43   
 *
 */
@Component
public class RedisAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {
	
	/**
	 * redis缓存授权码
	 */
	private static final String REDIS_AUTHORIZATION_CODE_PREFIX = "oauth:code:";
	
	@Autowired
	@Qualifier("objectRedisTemplate")
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 替换JdbcAuthorizationCodeServices的存储策略 将存储code到redis，并设置过期时间，10分钟<br>
	 */
	@Override
	protected void store(String code, OAuth2Authentication authentication) {
		redisTemplate.opsForValue().set(redisKey(code), authentication, 10, TimeUnit.MINUTES);
	}

	@Override
	protected OAuth2Authentication remove(final String code) {

		String codeKey = redisKey(code);
		OAuth2Authentication token = (OAuth2Authentication) redisTemplate.opsForValue().get(codeKey);
		this.redisTemplate.delete(codeKey);
		return token;
	}

	/**
	 * redis中 code key的前缀
	 * 
	 * @param code
	 * @return
	 */
	private String redisKey(String code) {
		return REDIS_AUTHORIZATION_CODE_PREFIX + code;
	}
}
