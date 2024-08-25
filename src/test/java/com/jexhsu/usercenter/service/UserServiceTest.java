package com.jexhsu.usercenter.service;

import com.jexhsu.usercenter.mapper.UserMapper;
import com.jexhsu.usercenter.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userServiceMocks;

    @Resource
    private UserService userService;

    /**
     * 测试用户注册的各种情况
     */
    /**
     * 测试正常注册成功
     */
    @Test
    public void testUserRegister_Success() {
        String userAccount = "jexhsu";
        String userPassword = "xujiwsdfr";
        String checkPassword = "xujiwsdfr";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        assertTrue(result > 0, "The result should be greater than zero");
        // TODO: delete testUser
    }

    /**
     * 测试账户为空的情况
     */
    @Test
    public void testUserRegister_AccountIsBlank() {
        String userAccount = "";
        String userPassword = "password123";
        String checkPassword = "password123";
        long result = userServiceMocks.userRegister(userAccount, userPassword, checkPassword);
        assertEquals(-1, result);
    }

    /**
     * 测试密码长度不足的情况
     */
    @Test
    public void testUserRegister_PasswordTooShort() {
        String userAccount = "testUser";
        String userPassword = "short";
        String checkPassword = "short";
        long result = userServiceMocks.userRegister(userAccount, userPassword, checkPassword);
        assertEquals(-1, result);
    }

    /**
     * 测试密码长度太长的情况
     */
    @Test
    public void testUserRegister_PasswordTooLong() {
        String userAccount = "testUser";
        String userPassword = "thePasswordTooLong";
        String checkPassword = "thePasswordTooLong";
        long result = userServiceMocks.userRegister(userAccount, userPassword, checkPassword);
        assertEquals(-1, result);
    }

    /**
     * 测试账户名包含特殊字符的情况
     */
    @Test
    public void testUserRegister_AccountContainsSpecialCharacters() {
        String userAccount = "test@User";
        String userPassword = "password123";
        String checkPassword = "password123";
        long result = userServiceMocks.userRegister(userAccount, userPassword, checkPassword);
        assertEquals(-1, result);
    }

    /**
     * 测试密码与确认密码不匹配的情况
     */
    @Test
    public void testUserRegister_PasswordsDoNotMatch() {
        String userAccount = "testUser";
        String userPassword = "password123";
        String checkPassword = "differentPassword";
        long result = userServiceMocks.userRegister(userAccount, userPassword, checkPassword);
        assertEquals(-1, result);
    }

    /**
     * 测试账户已经存在的情况
     */
    @Test
    public void testUserRegister_AccountAlreadyExists() {
        String userAccount = "jexhsu";
        String userPassword = "xujiwsdfr";
        String checkPassword = "xujiwsdfr";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        assertEquals(-1, result);
    }
}
