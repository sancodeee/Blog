package com.ws.service;

import com.ws.po.User;


public interface UserService {

    User checkUser(String username, String password);

}
