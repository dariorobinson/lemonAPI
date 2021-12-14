package com.revature.lemon.common.util.aspects;

import com.revature.lemon.common.dtos.ErrorResponse;
import com.revature.lemon.common.exceptions.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorResponseAspect {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler
    public ErrorResponse handleAuthenticationException(AuthenticationException e) {
        return new ErrorResponse(401, e);
    }
}
