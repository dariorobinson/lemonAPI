package com.revature.lemon.user;


import com.revature.lemon.auth.TokenService;
import com.revature.lemon.user.dtos.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public AuthController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @CrossOrigin(exposedHeaders = "Authorization")
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public LoginRequest authenticate(@RequestBody LoginRequest loginRequest, HttpServletResponse resp) {
        User authUser = userService.login(loginRequest);
        LoginRequest payload = new LoginRequest(authUser);
        String token = tokenService.generateToken(payload);
        resp.setHeader("Authorization", token);

        return payload;

    }

}
