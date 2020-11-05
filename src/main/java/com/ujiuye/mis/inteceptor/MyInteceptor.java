package com.ujiuye.mis.inteceptor;

import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MyInteceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Integer user =(Integer) session.getAttribute("active");
        if(user==null || user == 0 ){
            response.sendRedirect("/login.html");
            return false;
        }
        return true;
    }
}
