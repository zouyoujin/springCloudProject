package com.kitty.springcloud.oauth.server.service.impl;

import com.kitty.springcloud.oauth.server.entity.Users;
import com.kitty.springcloud.oauth.server.mapper.UsersMapper;
import com.kitty.springcloud.oauth.server.service.UsersService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author kitty
 * @since 2018-07-02
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

}
