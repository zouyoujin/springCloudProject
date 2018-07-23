package com.kitty.springcloud.oauth.server.config;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.RedirectMismatchException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedResponseTypeException;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitty.springcloud.oauth.server.security.RedisTemplateTokenStore;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: WebSecurityHandlerConfig
 * @Description:WebSecurity相关回调配置处理
 * @author: Kitty
 * @date: 2018年7月22日 上午2:53:18
 *
 */
@Slf4j
@Component
@Configuration
public class WebSecurityHandlerConfig {

	@Resource
	private ObjectMapper objectMapper; // springmvc启动时自动装配json处理类

	@Autowired
	@Qualifier("objectRedisTemplate")
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * redis缓存token处理逻辑
	 * 
	 * @return
	 */
	@Bean
	public RedisTemplateTokenStore redisTemplateStore() {
		// return new RedisTokenStore(connectionFactory);
		RedisTemplateTokenStore redisTemplateStore = new RedisTemplateTokenStore();
		redisTemplateStore.setRedisTemplate(redisTemplate);
		return redisTemplateStore;
	}

	// @Autowired
	// private ClientDetailsService clientDetailsService;
	//
	// @Autowired(required = false)
	// private AuthenticationEntryPoint authenticationEntryPoint;
	//
	// // url匹配器
	// private AntPathMatcher pathMatcher = new AntPathMatcher();

	/**
	 * 登陆成功，返回Token 装配此bean不支持授权码模式
	 * 
	 * @return
	 */
	@Bean
	public AuthenticationSuccessHandler loginSuccessHandler() {
		return new SavedRequestAwareAuthenticationSuccessHandler() {

			private RequestCache requestCache = new HttpSessionRequestCache();

			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				super.onAuthenticationSuccess(request, response, authentication);
				log.info("..........onAuthenticationSuccess.........." + requestCache);
				return;
			}
		};
	}

	/**
	 * 登陆失败
	 * 
	 * @return
	 */
	@Bean
	public AuthenticationFailureHandler loginFailureHandler() {
		return new AuthenticationFailureHandler() {
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				String msg = null;
				if (exception instanceof BadCredentialsException) {
					msg = "用户名或密码错误";
				} else {
					msg = exception.getMessage();
				}
				Map<String, String> rsp = new HashMap<>();
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				rsp.put("resp_code", HttpStatus.UNAUTHORIZED.value() + "");
				rsp.put("rsp_msg", msg);
				response.setContentType("application/json;charset=UTF-8");
				response.getWriter().write(objectMapper.writeValueAsString(rsp));
				response.getWriter().flush();
				response.getWriter().close();
			}
		};

	}

	@Bean
	public WebResponseExceptionTranslator webResponseExceptionTranslator() {
		return new DefaultWebResponseExceptionTranslator() {

			public static final String BAD_MSG = "坏的凭证";

			@Override
			public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
				// e.printStackTrace();
				OAuth2Exception oAuth2Exception;
				if (e.getMessage() != null && e.getMessage().equals(BAD_MSG)) {
					oAuth2Exception = new InvalidGrantException("用户名或密码错误", e);
				} else if (e instanceof InternalAuthenticationServiceException) {
					oAuth2Exception = new InvalidGrantException(e.getMessage(), e);
				} else if (e instanceof RedirectMismatchException) {
					oAuth2Exception = new InvalidGrantException(e.getMessage(), e);
				} else if (e instanceof InvalidScopeException) {
					oAuth2Exception = new InvalidGrantException(e.getMessage(), e);
				} else {
					oAuth2Exception = new UnsupportedResponseTypeException("服务内部错误", e);
				}

				ResponseEntity<OAuth2Exception> response = super.translate(oAuth2Exception);
				ResponseEntity.status(oAuth2Exception.getHttpErrorCode());
				response.getBody().addAdditionalInformation("resp_code", oAuth2Exception.getHttpErrorCode() + "");
				response.getBody().addAdditionalInformation("resp_msg", oAuth2Exception.getMessage());

				return response;
			}

		};
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new LogoutSuccessHandler() {
			@Override
			public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				if(StringUtils.isNotBlank(request.getHeader("referer")))
				{
					response.sendRedirect(request.getHeader("referer"));
				}
			}
		};
	}

	@Bean
	public LogoutHandler logoutHandler() {
		return new LogoutHandler() {

			@Autowired
			@Qualifier("redisTemplateStore")
			private RedisTemplateTokenStore redisTemplateStore;

			@Override
			public void logout(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) {
				Assert.notNull(redisTemplateStore, "tokenStore must be set");
				String token = extractToken(request);
				if(StringUtils.isBlank(token))
				{
					log.warn("token is empty! clear token info fail.");
					return;
				}
				OAuth2AccessToken existingAccessToken = redisTemplateStore.readAccessToken(token);
				OAuth2RefreshToken refreshToken;
				if (existingAccessToken != null) {
					if (existingAccessToken.getRefreshToken() != null) {
						log.info("remove refreshToken!", existingAccessToken.getRefreshToken());
						refreshToken = existingAccessToken.getRefreshToken();
						redisTemplateStore.removeRefreshToken(refreshToken);
					}
					log.info("remove existingAccessToken!", existingAccessToken);
					redisTemplateStore.removeAccessToken(existingAccessToken);
				}
				// response.sendRedirect(request.getHeader("referer"));
			}
		};
	}

	protected String extractToken(HttpServletRequest request) {
		// first check the header...
		String token = extractHeaderToken(request);
		if (token == null) {
			log.debug("Token not found in headers. Trying request parameters.");
			token = request.getParameter(OAuth2AccessToken.ACCESS_TOKEN);
		}
		// bearer type allows a request parameter as well
		if (token == null) {
			log.debug("Token not found in request parameter. Not an OAuth2 request.");
		} else {
			request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, OAuth2AccessToken.BEARER_TYPE);
		}

		return token;
	}

	protected String extractHeaderToken(HttpServletRequest request) {
		Enumeration<String> headers = request.getHeaders("Authorization");
		while (headers.hasMoreElements()) { // typically there is only one (most
											// servers enforce that)
			String value = headers.nextElement();
			if ((value.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase()))) {
				String authHeaderValue = value.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
				// Add this here for the auth details later. Would be better to
				// change the signature of this method.
				request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE,
						value.substring(0, OAuth2AccessToken.BEARER_TYPE.length()).trim());
				int commaIndex = authHeaderValue.indexOf(',');
				if (commaIndex > 0) {
					authHeaderValue = authHeaderValue.substring(0, commaIndex);
				}
				return authHeaderValue;
			}
		}

		return null;
	}

}
