package com.jexhsu.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jexhsu.usercenter.model.domain.User;
import com.jexhsu.usercenter.service.UserService;
import com.jexhsu.usercenter.common.ErrorCode;
import com.jexhsu.usercenter.exception.BusinessException;
import com.jexhsu.usercenter.mapper.UserMapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import static com.jexhsu.usercenter.constant.UserConstant.SALT;
import static com.jexhsu.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author xuji
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-08-25 11:57:15
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MIN_USERNAME_LENGTH = 4;
    public static final int MAX_PASSWORD_LENGTH = 16;

    /**
     * 用户注册
     *
     * @param user_account   用户账户
     * @param user_password  用户密码
     * @param check_password 校验密码
     * @return
     */
    @Override
    public long userRegister(String user_account, String user_password, String check_password) {
        // 非空校验
        if (StringUtils.isAnyBlank(user_account, user_password, check_password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        // 账号长度不小于4位
        if (user_account.length() < MIN_USERNAME_LENGTH) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度小于4位");
        }
        // 密码不小于8位
        if (user_password.length() < MIN_PASSWORD_LENGTH || check_password.length() < MIN_PASSWORD_LENGTH) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码小于8位");
        }
        // 密码不大于16位
        if (user_password.length() > MAX_PASSWORD_LENGTH || check_password.length() > MAX_PASSWORD_LENGTH) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户编号大于16位");
        }
        // 账户不包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        // 使用正则表达式进行校验
        Matcher matcher = Pattern.compile(validPattern).matcher(user_account);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号含有特殊字符");
        }
        // 密码和校验密码是否相同
        if (!user_password.equals(check_password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");
        }
        // 账户名称不能重复，查询数据库当中是否存在相同名称用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", user_account);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号名称已存在");
        }
        // 对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + user_password).getBytes());
        // 将数据插入数据库
        User user = new User();
        user.setUser_account(user_account);
        user.setUser_password(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "保存数据库失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String user_account, String user_password, HttpServletRequest request) {
        // 非空校验
        if (StringUtils.isAnyBlank(user_account, user_password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号密码不能为空");
        }
        // 账号长度不小于4位
        if (user_account.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度小于8位");
        }
        // 密码不小于8位
        if (user_password.length() < MIN_PASSWORD_LENGTH) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码小于8位");
        }
        // 密码不大于16位
        if (user_password.length() > MAX_PASSWORD_LENGTH) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户编号大于16位");
        }
        // 账户不包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        // 使用正则表达式进行校验
        Matcher matcher = Pattern.compile(validPattern).matcher(user_account);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号包含特殊字符");
        }
        // 对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + user_password).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", user_account);
        queryWrapper.eq("user_password", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("User login failed, userAccount cannot match user_password");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不存在或密码不正确");
        }
        // 用户信息脱敏
        User safetyUser = getSafetyUser(user);
        // 用户登录成功,将登录态设置到Session当中
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }
        Long id = currentUser.getId();
        User users = this.getById(id);
        if (users == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }
        return this.getSafetyUser(users);
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUser_name(originUser.getUser_name());
        safetyUser.setUser_account(originUser.getUser_account());
        safetyUser.setAvatar_url(originUser.getAvatar_url());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUser_role(originUser.getUser_role());
        safetyUser.setUser_status(originUser.getUser_status());
        safetyUser.setCreate_time(originUser.getCreate_time());
        return safetyUser;
    }
}
