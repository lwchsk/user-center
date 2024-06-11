package com.hsk.usercenter;

import com.hsk.usercenter.entity.domain.User;
import com.hsk.usercenter.mapper.UserMapper;
import com.hsk.usercenter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class UserCenterApplicationTests {

    @Resource
    private UserMapper userMapper;
    @Resource
    UserService userService;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.isTrue(1 == userList.size(), "");
        userList.forEach(System.out::println);
    }

    @Test
    void uerRegister() {
        String userName = "hsk";
        String userPassword = "12345678";
        String checkPassword = null;
        long result = userService.uerRegister(userName, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
    }


}
