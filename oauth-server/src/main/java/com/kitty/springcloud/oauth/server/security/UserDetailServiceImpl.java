package com.kitty.springcloud.oauth.server.security;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

	// @Autowired
	// private SysUserRepository sysUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("-----loadUserByUsername--------");

		if ("admin".equalsIgnoreCase(username)) {
			User user = mockUser();
			return user;
		}
		return null;
		
//		String lowcaseUsername = username.toLowerCase();
//		Optional<SysUser> realUser = Optional.of(new SysUser());
//		return realUser.map(user -> {
//			Set<GrantedAuthority> grantedAuthorities = user.getAuthorities();
//			return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
//		}).orElseThrow(() -> new UsernameNotFoundException("用户" + lowcaseUsername + "不存在!"));
	}
	
	private User mockUser() {
		Collection<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("admin"));
		
		User user = new User("admin",new BCryptPasswordEncoder().encode("123456"),authorities);
		return user;
	}
}
