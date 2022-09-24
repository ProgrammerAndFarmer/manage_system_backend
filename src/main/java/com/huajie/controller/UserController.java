package com.huajie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huajie.dto.UserDto;
import com.huajie.entry.Result;
import com.huajie.entry.User;
import com.huajie.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 用户登录
     * @param httpServletRequest
     * @param userDto
     * @return
     */
    @PostMapping("/login")
    public Result<User> login(HttpServletRequest httpServletRequest, @RequestBody UserDto userDto) {
        userDto = userService.userLogin(httpServletRequest, userDto);
        return Result.success(userDto);
    }

    @GetMapping("/logout")
    public Result<String> logout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute("user");
        return Result.success("退出成功！");
    }

    /**
     * 根据 id 查询用户
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public Result<User> getUserById(@PathVariable Long userId) {
        User user = userService.getById(userId);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("未查询到相关用户信息！");
        }
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param userName
     * @param emailAddress
     * @param phoneNumber
     * @return
     */
    @GetMapping("/page")
    public Result<Page> getPage(int page, int pageSize, String userName, String emailAddress, String phoneNumber) {
        Page<User> pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(userName), User::getUserName, userName);
        queryWrapper.like(StringUtils.isNotEmpty(emailAddress), User::getEmailAddress, emailAddress);
        queryWrapper.like(StringUtils.isNotEmpty(phoneNumber), User::getPhoneNumber, phoneNumber);
        queryWrapper.orderByDesc(User::getUpdateTime);

        userService.page(pageInfo, queryWrapper);

        return Result.success(pageInfo);
    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    @PostMapping
    public Result<String> addUser(@RequestBody User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, user.getUserName());
        boolean isExist = userService.getOne(queryWrapper) != null;
        if (isExist) {
            return Result.error("该用户名已被使用，请修改！");
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        } else {
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        }
        userService.save(user);
        return Result.success("用户添加成功！");
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        return addUser(user);
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody User user) {
        userService.updateById(user);
        return Result.success("用户信息修改成功！");
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @DeleteMapping("/del")
    public Result<String> deleteById(@RequestParam("id") Long userId) {
        boolean flag = userService.removeById(userId);
        if (flag) {
            return Result.success("用户删除成功！");
        } else {
            return Result.error("用户删除失败！");
        }
    }

    /**
     * 批量删除用户
     * @param ids
     * @return
     */
    @DeleteMapping("/del/batch")
    public Result<String> deleteBatchUsers(@RequestBody List<Long> ids) {
        boolean flag = userService.removeByIds(ids);
        if (flag) {
            return Result.success("用户批量删除成功！");
        } else {
            return Result.error("用户批量删除失败！");
        }
    }

}
