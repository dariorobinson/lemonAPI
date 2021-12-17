package com.revature.lemon.common.util.web;

import java.lang.annotation.*;

/**
 * Annotation used to check if user is logged in before they do a certain action.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authenticated {

}
