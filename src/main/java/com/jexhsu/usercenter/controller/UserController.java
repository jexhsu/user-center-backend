package com.jexhsu.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jexhsu.usercenter.common.BaseResponse;
import com.jexhsu.usercenter.common.ErrorCode;
import com.jexhsu.usercenter.exception.BusinessException;
import com.jexhsu.usercenter.model.domain.User;
import com.jexhsu.usercenter.model.request.UserLoginRequest;
import com.jexhsu.usercenter.model.request.UserRegisterRequest;
import com.jexhsu.usercenter.model.request.UserUpdateRequest;
import com.jexhsu.usercenter.model.request.UserdeleteRequest;
import com.jexhsu.usercenter.service.UserService;
import com.jexhsu.usercenter.utils.ResultUtils;
import com.jexhsu.usercenter.utils.ThrowUtils;

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
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String userAccount = userRegisterRequest.getUser_account();
        String userPassword = userRegisterRequest.getUser_password();
        String checkPassword = userRegisterRequest.getCheck_password();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long userId = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(userId);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String userAccount = userLoginRequest.getUser_account();
        String userPassword = userLoginRequest.getUser_password();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNoneBlank(username)) {
            queryWrapper.like("user_name", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> safetyUserList = new ArrayList<>();
        for (User user : userList) {
            safetyUserList.add(userService.getSafetyUser(user));
        }
        return ResultUtils.success(safetyUserList);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody UserdeleteRequest id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限");
        }
        if (id.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
            HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限");
        }
        if (userUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        User result = userService.getLoginUser(request);
        return ResultUtils.success(result);
    }

    private boolean isAdmin(HttpServletRequest request) {
        User users = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return users != null && users.getUser_role() == ADMIN_USER;
    }
}
