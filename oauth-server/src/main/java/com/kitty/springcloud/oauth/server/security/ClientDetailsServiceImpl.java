package com.kitty.springcloud.oauth.server.security;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.kitty.springcloud.common.utils.JsonUtils;
import com.kitty.springcloud.oauth.server.entity.OauthClientDetails;
import com.kitty.springcloud.oauth.server.service.OauthClientDetailsService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 自定义实现ClientDetailsService
 * 
 * @author kitty
 *
 */
@Slf4j
@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {

	/**
	 * 缓存client的redis key，这里是hash结构存储
	 */
	private static final String CACHE_CLIENT_KEY = "tbl_oauth_client_details";

	@Autowired
	private OauthClientDetailsService oauthClientDetailsService;

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		log.debug("ClientDetailsServiceImpl===>loadClientByClientId()============clientId=" + clientId);

		OauthClientDetails oauthClientDetails;
		// 先从redis获取
		String value = (String) redisTemplate.boundHashOps(CACHE_CLIENT_KEY).get(clientId);
		if (StringUtils.isEmpty(value)) {
			oauthClientDetails = cacheAndGetClient(clientId);
		} else {
			oauthClientDetails = JsonUtils.toObject(value, OauthClientDetails.class);
		}
		if (oauthClientDetails == null) {
			throw new NoSuchClientException("No client with requested id: " + clientId);
		}
		// resourceIds默认先写成*
		// String resourceIds =
		// String.valueOf(oauthClientDetails.getResourceIds());
		String resourceIds = "*";
		String scopes = oauthClientDetails.getScope(); // 允许的授权范围
		String grantTypes = oauthClientDetails.getAuthorizedGrantTypes();// 该client允许的授权类型
		String authorities = oauthClientDetails.getAuthorities();
		String redirectUris = oauthClientDetails.getWebServerRedirectUri();
		Integer refreshTokenValiditySeconds = oauthClientDetails.getRefreshTokenValidity();
		Integer accessTokenValiditySeconds = oauthClientDetails.getAccessTokenValidity();
		String clientSecret = oauthClientDetails.getClientSecret();

		BaseClientDetails baseClientDetails = new BaseClientDetails(clientId, resourceIds, scopes, grantTypes,
				authorities, redirectUris);
		baseClientDetails.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
		baseClientDetails.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
		baseClientDetails.setClientSecret(clientSecret);

		// 如果设置了自动审批则不需要授权审批页面
		if (!StringUtils.isEmpty(oauthClientDetails.getAutoapprove())) {
			Set<String> autoApproveScopes = StringUtils.commaDelimitedListToSet(oauthClientDetails.getAutoapprove());
			baseClientDetails.setAutoApproveScopes(autoApproveScopes);
		}

		return baseClientDetails;
	}

	/**
	 * 缓存client并返回client
	 *
	 * @param clientId
	 * @return
	 */
	private OauthClientDetails cacheAndGetClient(String clientId) {
		// 从数据库读取
		OauthClientDetails oauthClientDetails = null;
		try {
			EntityWrapper<OauthClientDetails> wrapper = new EntityWrapper<OauthClientDetails>();
			wrapper.eq("client_id", clientId);
			oauthClientDetails = oauthClientDetailsService.selectOne(wrapper);
			if (oauthClientDetails != null) {
				// 写入redis缓存
				redisTemplate.boundHashOps(CACHE_CLIENT_KEY).put(clientId, JsonUtils.toJson(oauthClientDetails));
				log.info("缓存clientId:{},{}", clientId, oauthClientDetails);
			}
		} catch (NoSuchClientException e) {
			log.error("clientId:{},{}", clientId, clientId);
			throw new NoSuchClientException("No client with requested id: " + clientId);
		} catch (InvalidClientException e) {
			log.error("clientId:{},{}", clientId, clientId);
			throw new NoSuchClientException("InvalidClientException with requested id: " + clientId);
		}

		return oauthClientDetails;
	}

	/**
	 * 将oauth_client_details全表刷入redis
	 */
	public void loadAllClientToCache() {
		if (redisTemplate.hasKey(CACHE_CLIENT_KEY)) {
			return;
		}
		log.info("将tbl_oauth_client_details全表刷入redis");

		List<OauthClientDetails> list = oauthClientDetailsService.selectList(null);
		if (CollectionUtils.isEmpty(list)) {
			log.error("oauth_client_details表数据为空，请检查");
			return;
		}

		list.parallelStream().forEach(client -> {
			redisTemplate.boundHashOps(CACHE_CLIENT_KEY).put(client.getClientId(), JsonUtils.toJson(client));
		});
	}

}