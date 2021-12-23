package com.pelr.socialnetwork_extins.repository;

/**
 * RepositoryException exception class
 */

public class RepositoryException extends RuntimeException{

    /**
     * Creates a RepositoryException exception with specific message.
     * @param message - message string
     */

    public RepositoryException(String message) {
        super(message);
    }
}
