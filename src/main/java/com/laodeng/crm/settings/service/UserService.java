package com.laodeng.crm.settings.service;

import com.laodeng.crm.exception.LoginException;
import com.laodeng.crm.settings.domain.User;

import java.util.List;

public interface UserService {

    User login(String loginAct, String loginPwd, String userIp, String sysTime) throws LoginException;

    List<User> getUserList();

}
