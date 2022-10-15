package com.shultzlab.weighttrackerapi.services;

import com.nimbusds.jwt.SignedJWT;
import com.shultzlab.weighttrackerapi.models.requests.CreateUserRequest;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class TokenService {
    public static String getUsernameFromToken(String token) {
        try {
            var decodedJWT = SignedJWT.parse(token);
            var payload = decodedJWT.getPayload().toJSONObject();
            String username = (String) payload.get("username");
            return username;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String registerUserWithAuthApi(CreateUserRequest user) {
        String username = user.getUsername();
        String password = user.getPassword();

        Map<String, String> bodyMap = new HashMap();
        bodyMap.put("username", username);
        bodyMap.put("password", password);

        String url = "https://auth-api-go.shultzlab.com/register";

        WebClient webClient = WebClient.create(url);

        try {
            webClient
                    .post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(bodyMap))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }

        return username;
    }

    public static String deleteUserWithAuthApi(String username) {
        String url = "https://auth-api-go.shultzlab.com/app/user/" + username;

        WebClient webClient = WebClient.create(url);

        try {
            webClient
                    .delete()
                    .header("X-API-Token", System.getenv("X_API_TOKEN"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return username;

    }
}
