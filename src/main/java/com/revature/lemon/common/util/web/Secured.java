package com.revature.lemon.common.util.web;


import com.revature.lemon.common.util.RoleType;

import java.lang.annotation.*;

/**
 * Annotation used to secure specific methods by user roles
 * todo perhaps change this name to Authorized? fits with the HttpStatus code naming convention
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Secured {
    RoleType[] allowedAccountTypes();
}
