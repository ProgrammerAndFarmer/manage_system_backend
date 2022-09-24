package com.huajie.interceptor;

import cn.hutool.setting.SettingRuntimeException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.huajie.entry.User;
import com.huajie.exceptionHandle.ServiceException;
import com.huajie.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) {
        String token = httpServletRequest.getHeader("token");
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //执行认证
        if (StringUtils.isEmpty(token)) {
            throw new ServiceException("无token， 请重新登录");
        }
        //获取 token 中的 userId
        Long userId;
        try {
            userId = Long.parseLong(JWT.decode(token).getAudience().get(0));
        } catch (JWTDecodeException j) {
            throw new ServiceException("无token， 请重新登录");
        }
        User user = userService.getById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在！");
        }

        //验证token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException j) {
            throw new SettingRuntimeException("密码错误，拒绝访问！");
        }
        return true;
    }
}
