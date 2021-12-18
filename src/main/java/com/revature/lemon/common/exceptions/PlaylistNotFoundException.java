package com.revature.lemon.common.exceptions;

/**
 * More specific exception to the ResourceNotFoundException because this was commonly used
 */
public class PlaylistNotFoundException extends RuntimeException {

    public PlaylistNotFoundException() {
        super("No playlist was found using using that id");
    }

    public PlaylistNotFoundException(String msg) {
        super(msg);
    }
}
