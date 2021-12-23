package com.pelr.socialnetwork_extins.domain;

/**
 * FriendNotFound exception class
 */

public class FriendNotFound extends RuntimeException{
    /**
     * Creates FriendNotFound exception with specified message.
     * @param message - message string
     */

    public FriendNotFound(String message) {
        super(message);
    }
}
