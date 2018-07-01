package com.kitty.springcloud.oauth.server.security;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

/**
 * 
 * 自定义实现ClientDetailsService
 * 
 * @author kitty
 *
 */
@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

		/*
		 * // 使用mybatic验证client是否存在 ，根据需求写sql Map clientMap =
		 * applyService.findApplyById(applyName); if(clientMap == null) { throw
		 * new NoSuchClientException("No client with requested id: " +
		 * clientId); }
		 */

//		JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
//		ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(applyName);
		ClientDetails details = null;
		return details;
	}

}