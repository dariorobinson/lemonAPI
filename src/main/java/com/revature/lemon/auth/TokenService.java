package com.revature.lemon.auth;

import com.revature.lemon.common.exceptions.AuthenticationException;
import com.revature.lemon.user.dtos.LoginRequest;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final TokenGenerator tokenGenerator;
    private final TokenValidator tokenValidator;

    public TokenService(TokenGenerator tokenGenerator, TokenValidator tokenValidator) {
        this.tokenGenerator = tokenGenerator;
        this.tokenValidator = tokenValidator;
    }

    public String generateToken(LoginRequest subject) {
        return tokenGenerator.createToken(subject);
    }

    public boolean isTokenValid(String token) {

        if (token == null || token.trim().equals("")) {
            return false;
        }

        token = token.replaceAll("Bearer ", "");

        return tokenValidator.parseToken(token)
                .isPresent();
    }

    public LoginRequest extractTokenDetails(String tokenHeader) {
        System.out.println(tokenHeader);

        if (tokenHeader == null || tokenHeader.trim().equals("")) {
            throw new AuthenticationException("No authentication token found on request!");
        }

        String token = tokenHeader.replaceAll("Bearer ", "");

        return tokenValidator.parseToken(token)
                .orElseThrow(RuntimeException::new);

    }


}
