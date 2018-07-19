package com.kitty.springcloud.oauth.server.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

	@Autowired
	private OauthClientDetailsService oauthClientDetailsService;
	
	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		log.debug("ClientDetailsServiceImpl===>loadClientByClientId()============clientId=" + clientId);
		
		EntityWrapper<OauthClientDetails> wrapper = new EntityWrapper<OauthClientDetails>();
		wrapper.eq("client_id", clientId);
		OauthClientDetails oauthClientDetails = oauthClientDetailsService.selectOne(wrapper);
		if(oauthClientDetails == null){
			throw new NoSuchClientException("No client with requested id: " + clientId);
		}
		//resourceIds默认先写成*
		//String resourceIds = String.valueOf(oauthClientDetails.getResourceIds());
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
		
		//如果跳转地址没有配置则不需要审批处理
		if(StringUtils.isBlank(redirectUris))
		{
			List<String> appScopeLists = new ArrayList<String>();
			appScopeLists.add("true");
			baseClientDetails.setAutoApproveScopes(appScopeLists);
		}
		
		return baseClientDetails;
	}

}