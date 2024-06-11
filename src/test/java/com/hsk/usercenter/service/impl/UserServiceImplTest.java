package com.hsk.usercenter.service.impl;

import com.hsk.usercenter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


/**
 * Date:2024/05/02
 * Author:hsk
 */
@SpringBootTest
class UserServiceImplTest {

    @Resource
    UserService userService;

    @Test
    void uerRegister() {
        String userAccount = "hsk1";
        String userPassword = "12345678";
        String checkPassword = null;
        long result = userService.uerRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userAccount = "hsk";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.uerRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userAccount = "hsk1";
        userPassword = "1234567";
        checkPassword = "1234567";
        result = userService.uerRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userAccount = "hskhsk";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.uerRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userAccount = "hsk1";
        userPassword = "12345678";
        checkPassword = "1234567";
        result = userService.uerRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userAccount = "hsk**";
        userPassword = "123456789";
        checkPassword = "123456789";
        result = userService.uerRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
    }
}