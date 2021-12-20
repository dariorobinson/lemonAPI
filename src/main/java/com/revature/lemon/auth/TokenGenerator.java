package com.revature.lemon.auth;

import com.revature.lemon.user.dtos.LoginRequest;
import io.jsonwebtoken.JwtBuilder;
import org.springframework.stereotype.Component;

@Component
public class TokenGenerator {

    private final JwtConfig jwtConfig;

    public TokenGenerator(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String createToken(LoginRequest loginRequest) {

        long now = System.currentTimeMillis();

        JwtBuilder tokenBuilder =Jwt

    }


}
