package com.pelr.socialnetwork_extins.service;

/**
 * UserNotFoundException exception class
 */

public class UserNotFoundException extends RuntimeException{

    /**
     * Creates a UserNotFoundException exception object containing specific message
     * @param message - message string
     */

    public UserNotFoundException(String message) {
        super(message);
    }
}
