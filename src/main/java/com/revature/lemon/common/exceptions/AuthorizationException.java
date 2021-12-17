package com.revature.lemon.common.exceptions;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException(String msg) {
        super();
    }

    public AuthorizationException() {
        super("You are not authorized to do this");
    }
}
