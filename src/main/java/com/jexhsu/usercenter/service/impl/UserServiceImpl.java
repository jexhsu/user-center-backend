package com.jexhsu.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jexhsu.usercenter.model.domain.User;
import com.jexhsu.usercenter.service.UserService;
import com.jexhsu.usercenter.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author xuji
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-08-25 11:57:15
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




