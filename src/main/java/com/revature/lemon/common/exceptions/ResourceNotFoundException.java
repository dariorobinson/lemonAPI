package com.revature.lemon.common.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("No resource found using the provided search criteria!");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
