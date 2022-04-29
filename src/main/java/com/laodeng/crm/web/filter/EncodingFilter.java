package com.laodeng.crm.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

// @WebFilter("/settings/user/login.do")
public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 处理前端请求中文乱码
        request.setCharacterEncoding("UTF-8");
        // 执行结束，处理响应流响应中文乱码
        response.setContentType("text/html; charset=UTF-8");
        // 处理结束，继续执行
        chain.doFilter(request, response);

    }
}
