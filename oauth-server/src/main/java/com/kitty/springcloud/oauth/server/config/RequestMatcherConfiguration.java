package com.kitty.springcloud.oauth.server.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 
 * @ClassName: RequestMatcherConfiguration   
 * @Description: oauth2 资源请求匹配器
 * @author: Kitty
 * @date: 2018年7月7日 下午12:03:57   
 *
 */
@Configuration
public class RequestMatcherConfiguration implements RequestMatcher {
	
    @Override
    public boolean matches(HttpServletRequest httpServletRequest) {
        String auth = httpServletRequest.getHeader("Authorization");
        //判断来源请求是否包含oauth2授权信息,这里授权信息来源可能是头部的Authorization值以Bearer开头,或者是请求参数中包含access_token参数,满足其中一个则匹配成功
        boolean haveOauth2Token = (auth != null) && auth.startsWith("Bearer");
        boolean haveAccessToken = httpServletRequest.getParameter("access_token")!=null;
        return haveOauth2Token || haveAccessToken;
    }
}
