package com.jexhsu.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jexhsu.usercenter.model.domain.User;
import com.jexhsu.usercenter.model.request.UserLoginRequest;
import com.jexhsu.usercenter.model.request.UserRegisterRequest;
import com.jexhsu.usercenter.model.request.UserUpdateRequest;
import com.jexhsu.usercenter.model.request.UserdeleteRequest;
import com.jexhsu.usercenter.service.UserService;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.jexhsu.usercenter.constant.UserConstant.ADMIN_USER;
import static com.jexhsu.usercenter.constant.UserConstant.USER_LOGIN_STATE;

@RestController()
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:8000", allowCredentials = "true")
public class UserController {
    @Resource
    UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ArrayList<User>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNoneBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> safetyUserList = new ArrayList<>();
        for (User user : userList) {
            safetyUserList.add(userService.getSafetyUser(user));
        }
        return safetyUserList;
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody UserdeleteRequest id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return false;
        }
        if (id.getId() <= 0) {
            return false;
        }
        return userService.removeById(id);
    }

    @PostMapping("/update")
    public Boolean updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
            HttpServletRequest request) {
        if (!isAdmin(request) || userUpdateRequest == null) {
            return false;
        }
        User user = new User();
        System.out.println(userUpdateRequest);
        BeanUtils.copyProperties(userUpdateRequest, user);
        System.out.println(user);
        boolean result = userService.updateById(user);
        return result;
    }

    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            return null;
        }
        Long id = currentUser.getId();
        User user = userService.getById(id);
        return userService.getSafetyUser(user);
    }

    private boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return user != null && user.getUserRole() == ADMIN_USER;
    }
}
