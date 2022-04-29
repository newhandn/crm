package com.laodeng.crm.web.filter;

import com.laodeng.crm.settings.domain.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
/*
* 过滤器，防止非法访问
* */
public class ServletFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        // 登录成功之后创建了会话域
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        // 获取访问路径
        String servletPath = request.getServletPath();
        System.out.println(servletPath);
        // 获取session对象，没有则返回null
        HttpSession session = request.getSession(false);
        User user = null;
        // 获取session对象中的user
        if(session != null){
            user = (User)session.getAttribute("user");
        }

        // jsp可能创建内置对象session
        if((session != null && user != null) || "/login.jsp".equals(servletPath) || "/settings/user/login.do".equals(servletPath)){
            // 向下继续执行
            chain.doFilter(req, res);
        }else{
            // 非法访问，重定向至登录页面
            /*

                重定向的路径怎么写？
                在实际项目开发中，对于路径的使用，不论操作的是前端还是后端，应该一律使用绝对路径
                    关于转发和重定向的路径的写法如下：
                        转发：
                            使用的是一种特殊的绝对路径的使用方式，这种绝对路径，前面不加 /项目名 ，这种路径也称之为内部路径
                            /login.jsp

                        重定向：
                            使用的是传统绝对路径的写法，前面必须以/项目名开头，后面跟具体的资源路径
                            /crm/login.jsp

                为什么使用重定向？转发不行吗？
                    转发之后，路径会停留在老路径上，而不是跳转之后最新资源的路径
                    我们应该在为用户跳转到登录页的同时，将浏览器的地址栏应该自动设置为当前的登录页的路径

             */
            response.sendRedirect(request.getContextPath());
        }

    }
}
