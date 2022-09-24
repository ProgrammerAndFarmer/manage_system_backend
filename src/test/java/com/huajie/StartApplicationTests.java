package com.huajie;

import com.huajie.entry.User;
import com.huajie.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StartApplicationTests {
    @Autowired
    UserService userService;

    @Test
    public void getUser() {
        User user = userService.getById(1);
        System.out.println(user);
    }

}
