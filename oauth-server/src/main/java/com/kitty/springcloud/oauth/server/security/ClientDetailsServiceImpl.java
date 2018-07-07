package com.kitty.springcloud.oauth.server.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.kitty.springcloud.oauth.server.entity.OauthClientDetails;
import com.kitty.springcloud.oauth.server.service.OauthClientDetailsService;

/**
 * 
 * 自定义实现ClientDetailsService
 * 
 * @author kitty
 *
 */
@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {

	@Autowired
	private OauthClientDetailsService oauthClientDetailsService;
	
	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

		/*
		 * // 使用mybatic验证client是否存在 ，根据需求写sql Map clientMap =
		 * applyService.findApplyById(applyName); if(clientMap == null) { throw
		 * new NoSuchClientException("No client with requested id: " +
		 * clientId); }
		 */

		// JdbcClientDetailsService jdbcClientDetailsService = new
		// JdbcClientDetailsService(dataSource);
		// ClientDetails clientDetails =
		// jdbcClientDetailsService.loadClientByClientId(applyName);

		// clients.inMemory() // 使用in-memory存储
		// .withClient("client") // client_id
		// .secret("secret") // client_secret
		// .authorizedGrantTypes("authorization_code") // 该client允许的授权类型
		// .scopes("app"); // 允许的授权范围
//		if (!clientId.equals("webapp")) {
//			throw new NoSuchClientException("No client with requested id: " + clientId);
//		}
		String resourceIds = "*";
		String scopes = "app";
		String grantTypes = "authorization_code,password,refresh_token";
		String authorities = "client";
		String redirectUris = "http://www.baidu.com";
		Integer refreshTokenValiditySeconds = 3600;
		Integer accessTokenValiditySeconds = 3600;
		String clientSecret = "webapp";
		
		EntityWrapper<OauthClientDetails> wrapper = new EntityWrapper<OauthClientDetails>();
		wrapper.eq("client_id", clientId);
		//wrapper.where("client_id = {0}",clientId);
		List<OauthClientDetails> list = oauthClientDetailsService.selectList(wrapper);
		BaseClientDetails baseClientDetails = new BaseClientDetails(clientId, resourceIds, scopes, grantTypes,
				authorities, redirectUris);
		baseClientDetails.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
		baseClientDetails.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
		baseClientDetails.setClientSecret(clientSecret);
		baseClientDetails.setClientId("webapp");
		baseClientDetails.setClientSecret("webapp");
		return baseClientDetails;
	}

}