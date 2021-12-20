package com.revature.lemon.auth;

import com.revature.lemon.user.dtos.LoginRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TokenValidator {

    private final JwtConfig jwtConfig;

    @Autowired
    public TokenValidator(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public Optional<LoginRequest> parseToken(String token) {

        try {

            Claims claims = Jwts.parser()
                                .setSigningKey(jwtConfig.getSigningKey())
                                .parseClaimsJws(token)
                                .getBody();

            return Optional.of(new LoginRequest(claims.getId(), claims.getSubject(), claims.get("discriminator", String.class)));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
