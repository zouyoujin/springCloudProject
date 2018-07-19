package com.kitty.springcloud.oauth.server.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: UsersController
 * @Description:用户控制层
 * @author: Kitty
 * @date: 2018年7月2日 上午1:31:44
 *
 */
@Slf4j
@RestController
public class UsersController {
	
	@GetMapping("/hello")
	public String user() {
		return "Hello World!";
	}

	@RequestMapping(value = { "/users" }) // 获取用户信息。/auth/user
	public Map<String, Object> user(OAuth2Authentication user) {
		Map<String, Object> userInfo = new HashMap<>();
		userInfo.put("user", user.getUserAuthentication().getPrincipal());
		log.debug("认证详细信息:" + user.getUserAuthentication().getPrincipal().toString());
		userInfo.put("authorities", AuthorityUtils.authorityListToSet(user.getUserAuthentication().getAuthorities()));
		return userInfo;
	}
	
	/**
	 * 用户相关信息
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/user/info" }) // 获取用户信息。/auth/user
	public Map<String, Object> user(Principal user) {
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) user;
		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("user", userDetails.getPrincipal());
		return userInfo;
	}
	
	/**
	 * 主页面
	 * @return
	 */
	@GetMapping("/")
	public ModelAndView home() {
		return new ModelAndView("index");
	}
}
