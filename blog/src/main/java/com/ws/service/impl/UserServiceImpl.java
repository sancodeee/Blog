package com.ws.service.impl;

import com.ws.dao.UserMapper;
import com.ws.po.User;
import com.ws.service.UserService;
import com.ws.util.MD5Utils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Cacheable(cacheNames = "user", key = "#username", unless = "#result == null ")
    @Override
    public User checkUser(String username, String password) {
        return userMapper.findByUsernameAndPassword(username, MD5Utils.code(password));
    }
}
