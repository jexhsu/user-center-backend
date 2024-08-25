package com.jexhsu.usercenter.service;
import java.util.Date;

import com.jexhsu.usercenter.model.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

   @Test
   public void testAddUser() {
       User user = new User();

       user.setUsername("jexhsu");
       user.setUserAccount("jexhsu");
       user.setAvatarUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ9pisrl5v6LFRE9Xk2qA_FvYDyLh12Z9vivg&s");
       user.setGender(1);
       user.setUserPassword("xujiwsdfr");
       user.setPhone("1779148xxxx");
       user.setEmail("jexhsu@gmail.com");
       user.setIsDelete(0);
       user.setUserRole(1);
       user.setPlanetCode("1");

       userService.save(user);
       System.out.println("user id : " + user.getId());
   }
}
