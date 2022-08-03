package com.shultzlab.weighttrackerapi.interceptors;

import com.shultzlab.weighttrackerapi.models.api.Token;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor extends HandlerInterceptorAdapter
{
    @Override
    public boolean preHandle(HttpServletRequest requestServlet, HttpServletResponse responseServlet, Object handler) throws Exception
    {

        // TODO is this being called twice when running on mac
        String url = "https://auth-api-go.shultzlab.com";
        String token = requestServlet.getHeader("x-auth-token");

        WebClient webClient = WebClient.create(url);

        webClient
            .get()
            .uri("/verify")
            .header("x-auth-token", token)
            .retrieve()
            .bodyToMono(Token.class)
            .block();

        return true;
    }

}

