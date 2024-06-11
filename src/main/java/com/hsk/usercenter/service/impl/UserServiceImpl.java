package com.hsk.usercenter.service.impl;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsk.usercenter.common.ErrorCode;
import com.hsk.usercenter.entity.domain.User;
import com.hsk.usercenter.exception.BusinessException;
import com.hsk.usercenter.service.UserService;
import com.hsk.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hsk.usercenter.constant.UserConstant.USER_LOGIN_STATE;


/**
 * @author dachui_boom
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-05-02 10:34:12
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final String SALT = "hsk";
    @Resource
    private UserMapper userMapper;

    @Override
    public long uerRegister(String userAccount, String userPassword, String checkPassword) {
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }
        if (userAccount.length() < 4)
            return -1;
        if (userPassword.length() < 8 || checkPassword.length() < 8)
            return -1;

        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }

        if (!checkPassword.equals(userPassword))
            return -1;

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0)
            return -1;

        String encryPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());


        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        return user.getId();

    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户和密码为空");
        }
        if (userAccount.length() < 4)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户小于四位");
        if (userPassword.length() < 8)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码小于八位");

        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能有特殊字符");
        }


        String encryPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户或密码错误");
        }

        User safetyUser = getSafetyUser(user);
        //4.向服务器记录登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    @Override
    public User getSafetyUser(User user) {
        //3.脱敏
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setUserRole(user.getUserRole());
        return safetyUser;

    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




