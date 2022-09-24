package com.huajie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huajie.dto.UserDto;
import com.huajie.entry.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {

    public UserDto userLogin(HttpServletRequest httpServletRequest, UserDto userDto);
}
