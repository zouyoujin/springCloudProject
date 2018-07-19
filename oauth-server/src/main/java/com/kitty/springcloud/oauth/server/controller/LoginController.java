package com.kitty.springcloud.oauth.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {

	/**
	 * 认证登录页面
	 * 
	 * @return ModelAndView
	 */
	@GetMapping("/login")
	public ModelAndView loginPage() {
		return new ModelAndView("login");
	}
}
