package com.laodeng.crm.settings.service.Impl;

import com.laodeng.crm.exception.LoginException;
import com.laodeng.crm.settings.dao.UserDao;
import com.laodeng.crm.settings.domain.User;
import com.laodeng.crm.settings.service.UserService;
import com.laodeng.crm.utils.MD5Util;
import com.laodeng.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String userIp, String sysTime)
            throws LoginException {

        // 密码要经过md5加密
        loginPwd = MD5Util.getMD5(loginPwd);

        // 将参数封装到Map集合中
        Map<String, String> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);

        // 调用dao层方法
        User user = userDao.selectLogin(map);

        if(user != null){
            // 如果user对象不为null，判断ip地址是否合法
            String ip = user.getAllowIps();
            if(ip.contains(userIp)){
                // 获取用户失效日期
                String allowTime = user.getExpireTime();
                // System.out.println(allowTime);
                // 判断 用户是否失效
                if(allowTime.compareTo(sysTime) > 0){
                    // 未失效
                    // 判断用户状态
                    if(user.getLockState().equals(1+"")){
                        // 用户未被锁定
                        // 用户登录成功
                        return user;
                    }else{
                        // 该用户被锁定
                        throw new LoginException("用户已被锁定");
                    }
                }else{
                    // 该用户已失效
                    throw new LoginException("该用户已失效");

                }
            }else{
                // 登录ip不合法
                throw new LoginException("IP不合法");
            }
        }else{
            // 如果user对象为null，表示用户名，密码错误
            throw new LoginException("账号密码错误");
        }

    }

    @Override
    public List<User> getUserList() {

        return userDao.getUserList();

    }

}
