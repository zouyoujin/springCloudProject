package com.kitty.springcloud.oauth.server.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {

	@Autowired
    @Qualifier("consumerTokenServices")
    private ConsumerTokenServices consumerTokenServices;
	
	/**
	 * 认证登录页面
	 * 
	 * @return ModelAndView
	 */
	@GetMapping("/login")
	public ModelAndView loginPage() {
		return new ModelAndView("login");
	}

	@RequestMapping("/exit")
	public void exit(HttpServletRequest request, HttpServletResponse response) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			new SecurityContextLogoutHandler().logout(request, response, authentication);
			OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
			consumerTokenServices.revokeToken(details.getTokenValue());
			response.sendRedirect(request.getHeader("referer"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
