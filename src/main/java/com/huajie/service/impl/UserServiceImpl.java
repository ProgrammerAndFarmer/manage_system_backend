package com.huajie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huajie.dto.UserDto;
import com.huajie.entry.Result;
import com.huajie.entry.User;
import com.huajie.exceptionHandle.ServiceException;
import com.huajie.mapper.UserMapper;
import com.huajie.service.UserService;
import com.huajie.utils.TokenUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public UserDto userLogin(HttpServletRequest httpServletRequest, UserDto userDto) {
        String pwd = userDto.getPassword();
        if (StringUtils.isEmpty(userDto.getUserName()) || StringUtils.isEmpty(userDto.getPassword())) {
            throw new ServiceException("请输入正确的用户名或密码！");
        }
        pwd = DigestUtils.md5DigestAsHex(pwd.getBytes());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userDto.getUserName());
        User user = this.getOne(queryWrapper);

        //验证数据库信息
        if (user == null) {
            throw new ServiceException("用户名不存在，请重新输入！");
        }

        if (!pwd.equals(user.getPassword())) {
            throw new ServiceException("密码错误，请重新输入！");
        }

        //拷贝基本信息
        BeanUtils.copyProperties(user, userDto);

        //采取token形式验证用户信息
        String token = TokenUtils.genToken(userDto.getId(), user.getPassword());
        userDto.setToken(token);

        //登录成功后，将用户信息放入session
        httpServletRequest.getSession().setAttribute("userInfo", userDto);
        return userDto;
    }
}
