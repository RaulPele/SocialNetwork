package com.pelr.socialnetwork_extins.domain.validators;

/**
 * ValidationException exception class
 */

public class ValidationException extends RuntimeException{

    /**
     * Creates a ValidationException exception with specified message
     * @param message - message string
     */

    public ValidationException(String message){
        super(message);
    }
}
