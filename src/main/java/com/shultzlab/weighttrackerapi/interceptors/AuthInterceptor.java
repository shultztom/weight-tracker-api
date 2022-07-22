package com.shultzlab.weighttrackerapi.interceptors;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor extends HandlerInterceptorAdapter
{
    @Override
    public boolean preHandle(HttpServletRequest requestServlet, HttpServletResponse responseServlet, Object handler) throws Exception
    {
        // TODO add auth
        // TODO is this being called twice when running on mac
        System.out.println("INTERCEPTOR PREHANDLE CALLED");
        return true;
    }

}

