package com.shultzlab.weighttrackerapi.services;

import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

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
}
