package com.pelr.socialnetwork_extins.service;

public class UserAlreadyExistsException extends  RuntimeException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
