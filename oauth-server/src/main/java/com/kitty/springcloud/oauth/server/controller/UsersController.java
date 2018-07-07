package com.kitty.springcloud.oauth.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @ClassName: UsersController
 * @Description:用户控制层
 * @author: Kitty
 * @date: 2018年7月2日 上午1:31:44
 *
 */
@RestController
@RequestMapping("/users")
public class UsersController {
	
	@GetMapping("/user")
	public String user() {
		
		return "Hello World!";
	}
}
