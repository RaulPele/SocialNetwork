package com.pelr.socialnetwork_extins.service;

/**
 * Authentication exception class
 */
public class AuthenticationException extends RuntimeException{

    /**
     * Creates authentication exception with specified message.
     * @param message - message string
     */
    public AuthenticationException(String message) {
        super(message);
    }
}
