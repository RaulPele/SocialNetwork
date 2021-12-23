package com.pelr.socialnetwork_extins.service;

import com.pelr.socialnetwork_extins.domain.User;
import com.pelr.socialnetwork_extins.repository.Repository;

/**
 * Authentication service class
 */
public class Authentication {
    private Repository<Long, User> userRepository;
    private User loggedUser;

    /**
     * Creates an authentication service for the specified user base.
     *
     * @param userRepository - Repository containing all the users
     */
    public Authentication(Repository<Long, User> userRepository) {
        this.userRepository = userRepository;
        this.loggedUser= null;
    }

    /**
     * Returns the current user.
     *
     * @return loggedUser - User object representing the current user
     * @throws AuthenticationException if there is no logged in user
     */
    public User getLoggedUser(){
        if(loggedUser == null){
            throw new AuthenticationException("You are not logged in!");
        }

        return loggedUser;
    }

    private User findUserByEmail(String email){
        Iterable<User> users = userRepository.findAll();
        User user = null;

        for(User currentUser : users){
            if(currentUser.getEmail().equals(email)){
                user = currentUser;
                break;
            }
        }

        return user;
    }

    /**
     * Logs in a user
     *
     * @param email - The email of the user that tries to log in
     * @throws UserNotFoundException if the specified user doesn't exist.
     */
    public void login(String email){
        loggedUser = findUserByEmail(email);

        if(loggedUser == null){
            throw new UserNotFoundException("User with specified email address doesn't exist!");
        }
    }

    /**
     * Logs out current user
     */
    public void logout(){
        this.loggedUser = null;
    }

    public boolean isLoggedIn(){
        return loggedUser != null;
    }

    //comentariu de proba
}
