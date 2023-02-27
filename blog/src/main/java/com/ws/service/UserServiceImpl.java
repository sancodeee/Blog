package com.ws.service;

import com.ws.dao.UserRepository;
import com.ws.po.User;
import com.ws.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Cacheable(cacheNames = "user", key = "#username", unless = "#result == null ")
    @Override
    public User checkUser(String username, String password) {

        return userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));

    }
}
