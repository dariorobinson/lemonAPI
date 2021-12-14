package com.revature.lemon.common.util.aspects;

import com.revature.lemon.common.exceptions.AuthenticationException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Aspect
@Component
public class SecurityAspect {

    /**
     * For methods with Authenticated, check if the use is logged in to access it.
     */
    @Order(1)
    @Before("@annotation(com.revature.lemon.common.util.web.Authenticated)")
    public void requireAuthentication() {
        AuthenticationException e = new AuthenticationException("No current sesssion found");
        HttpSession session = getCurrentSessionIfExist().orElseThrow(() -> e);
        System.out.println(session.getAttribute("authUser"));
        if(session.getAttribute("authUser") == null) throw e;
    }

    /**
     * Helper method for requireAnnotation() to get the current session
     */
    private Optional<HttpSession> getCurrentSessionIfExist() {
        return Optional.ofNullable(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession(false));
    }
}
