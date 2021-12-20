package com.revature.lemon.user;


import com.revature.lemon.user.dtos.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void authenticate(@RequestBody LoginRequest loginRequest, HttpSession httpSession) {
        User authUser = userService.login(loginRequest);
        httpSession.setAttribute("authUser", authUser);
        System.out.println("authenticate user is " + httpSession.getAttribute("authUser"));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void invalidateSession(HttpSession session) {
        session.invalidate();
    }


}
