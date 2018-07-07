package com.kitty.springcloud.oauth.server.service.impl;

import com.kitty.springcloud.oauth.server.entity.OauthClientDetails;
import com.kitty.springcloud.oauth.server.mapper.OauthClientDetailsMapper;
import com.kitty.springcloud.oauth.server.service.OauthClientDetailsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户端权限检验表 服务实现类
 * </p>
 *
 * @author kitty
 * @since 2018-07-02
 */
@Service
public class OauthClientDetailsServiceImpl extends ServiceImpl<OauthClientDetailsMapper, OauthClientDetails> implements OauthClientDetailsService {

}
