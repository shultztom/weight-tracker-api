package com.shultzlab.weighttrackerapi.interceptors;

import com.shultzlab.weighttrackerapi.exceptions.TokenForbiddenException;
import com.shultzlab.weighttrackerapi.models.api.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest requestServlet, HttpServletResponse responseServlet, Object handler) throws Exception
    {
        String url = "https://auth-api-go.shultzlab.com";
        String token = requestServlet.getHeader("x-auth-token");
        if(token == null){
            // Try other
            token = requestServlet.getHeader("X-Auth-Token");
        }

        WebClient webClient = WebClient.create(url);

        try {
            webClient
                    .get()
                    .uri("/verify")
                    .header("x-auth-token", token)
                    .retrieve()
                    .bodyToMono(Token.class)
                    .block();
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new TokenForbiddenException();
        }

        return true;
    }

}

