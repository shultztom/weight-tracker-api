package com.shultzlab.weighttrackerapi.services;

import com.nimbusds.jwt.SignedJWT;
import com.shultzlab.weighttrackerapi.exceptions.ResourceNotFoundException;
import com.shultzlab.weighttrackerapi.exceptions.TokenForbiddenException;
import com.shultzlab.weighttrackerapi.models.User;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class TokenService {
    private final UserService userService;

    public TokenService(UserService userService) {
        this.userService = userService;
    }

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

    public User getAndValidateUsernameFromToken(String token, String username) throws TokenForbiddenException, ResourceNotFoundException {
        String tokenUser = getUsernameFromToken(token);
        if(!username.equals(tokenUser)) {
            throw new TokenForbiddenException("Not authorized to access this goal");
        }

        return userService.findDistinctTopByUsername(username);
    }
}
