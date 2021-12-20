package com.revature.lemon.auth;

import com.revature.lemon.user.dtos.LoginRequest;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenGenerator {

    private final JwtConfig jwtConfig;

    public TokenGenerator(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String createToken(LoginRequest loginRequest) {

        long now = System.currentTimeMillis();

        JwtBuilder tokenBuilder = Jwts.builder()
                .setId(loginRequest.getId())
                .setSubject(loginRequest.getUsername())
                .setIssuer("lemon")
                .claim("discriminator", loginRequest.getDiscriminator())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtConfig.getExpiration()))
                .signWith(jwtConfig.getSigAlg(), jwtConfig.getSigningKey());

        return "Bearer " + tokenBuilder.compact();

    }


}
