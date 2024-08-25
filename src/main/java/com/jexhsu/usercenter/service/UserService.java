package com.jexhsu.usercenter.service;

import com.jexhsu.usercenter.model.domain.User;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author xuji
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2024-08-25 11:57:15
 */
public interface UserService extends IService<User> {

    long userRegister(String userAccount, String userPassword, String checkPassword);

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getSafetyUser(User originUser);
}
