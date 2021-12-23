package com.pelr.socialnetwork_extins.domain;

/**
 * FriendshipAlreadyExists exception class
 */

public class FriendshipAlreadyExists extends RuntimeException{

    /**
     * Creates a FriendshipAlreadyExists exception with specified message.
     * @param message - message string
     */

    public FriendshipAlreadyExists(String message) {
        super(message);
    }
}
