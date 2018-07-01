package com.kitty.springcloud.oauth.server.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author kitty
 * @since 2018-07-02
 */
@RestController
@RequestMapping("/users")
public class UsersController {
	@GetMapping("/user")
	public Principal user(Principal user) {
		return user;
	}
}
