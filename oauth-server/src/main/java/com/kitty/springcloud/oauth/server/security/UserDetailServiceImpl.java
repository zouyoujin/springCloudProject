package com.kitty.springcloud.oauth.server.security;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.kitty.springcloud.oauth.server.entity.CustomUserDetails;
import com.kitty.springcloud.oauth.server.entity.Users;
import com.kitty.springcloud.oauth.server.service.UsersService;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义实现UserDetailsService
 * 
 * @author kitty
 *
 */
@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UsersService usersService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("UserDetailServiceImpl===>loadUserByUsername()============username=" + username);
		Users user = null;
		EntityWrapper<Users> wrapper = new EntityWrapper<Users>();
		wrapper.eq("user_name", username);
		user = usersService.selectOne(wrapper);
		if(user == null){
			new UsernameNotFoundException("用户" + username + "不存在!");
		}
		//权限默认admin没有进行细分
		Collection<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("admin"));
		
		return new CustomUserDetails(user, true, true, true, true, authorities);
	}
}
