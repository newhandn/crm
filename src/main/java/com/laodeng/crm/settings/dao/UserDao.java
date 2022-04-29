package com.laodeng.crm.settings.dao;

import com.laodeng.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    User selectLogin(Map<String, String> map);

    List<User> getUserList();
}
