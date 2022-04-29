package com.laodeng.crm.settings.web.controller;

import com.laodeng.crm.settings.domain.User;
import com.laodeng.crm.settings.service.Impl.UserServiceImpl;
import com.laodeng.crm.settings.service.UserService;
import com.laodeng.crm.utils.DateTimeUtil;
import com.laodeng.crm.utils.PrintJson;
import com.laodeng.crm.utils.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet({"/settings/user/login.do"})
public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 获取前端访问的路径名
        String servletPath = request.getServletPath();

        if("/settings/user/login.do".equals(servletPath)){

            login(request, response);

        }
    }

    // 登录业务
    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 获取前端传来的数据
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");

        // 获取前端登录的ip
        String userIp = request.getRemoteAddr();
        // System.out.println(userIp);

        // 获取前端登录的时间
        String systemTime = DateTimeUtil.getSysTime();

        // 将上述参数传入到service，进行分析
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        try{
            // 调用业务层方法
            User user = userService.login(loginAct, loginPwd, userIp, systemTime);

            // 程序执行到这里，说明登录成功
            // 获取用户名，将其存入session中
            request.getSession().setAttribute("user", user);

            PrintJson.printJsonFlag(response, true);
        }catch(Exception e){
            e.printStackTrace();
            String msg = e.getMessage();

            // 创建Map集合
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("msg", msg);

            // 转为json
            PrintJson.printJsonObj(response, map);

            /*

                我们现在作为controller，需要为ajax请求提供多项信息

                可以有两种手段来处理
                    （1）将多项信息打包成map，将map解析成json串
                    （2）创建一个VO
                        private boolean success;
                        private String msg;

                     如果对于展现的信息将来还会大量地使用，我们创建一个VO类，使用方便
                     如果对于展现的信息只有在这个需求中能够使用，我们使用map就可以了。

             */

        }

    }
}