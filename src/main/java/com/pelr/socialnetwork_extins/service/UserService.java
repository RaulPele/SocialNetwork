package com.pelr.socialnetwork_extins.service;

import com.pelr.socialnetwork_extins.domain.User;
import com.pelr.socialnetwork_extins.domain.UserAuthCredentials;
import com.pelr.socialnetwork_extins.repository.Repository;
import com.pelr.socialnetwork_extins.repository.database.UserDBRepository;
import com.pelr.socialnetwork_extins.utils.PasswordEncryptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * UserService class for user service
 */

public class UserService {
    private UserDBRepository usersRepository;

    /**
     * Creates a user service that uses specified repository.
     * @param usersRepository - user repository
     */

    public UserService(UserDBRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    /**
     * Saves a user to repository.
     * @param firstName - first name string of user
     * @param lastName - last name string of user
     * @param email - email of user
     * @return savedUser - null if the entity was saved; user entity if the entity already exists.
     */

    public User save(String firstName, String lastName, String email, String password){
        User user = new User(firstName, lastName, email);
        PasswordEncryptor encryptor = new PasswordEncryptor();
        UserAuthCredentials authCredentials = new UserAuthCredentials(email, encryptor.generatePasswordHash(password));

        user.setAuthCredentials(authCredentials);

        //TODO: check if user exists

        return usersRepository.save(user);
    }

    /**
     * Removes a user from repository.
     * @param email - email of the user
     * @return removedUser - the user, if the removal is successful;
     * @throws  UserNotFoundException  if the specified user doesn't exist
     */

    public User remove(String email){
       User removedUser = usersRepository.remove(findIDByUserEmail(email));

       if(removedUser == null){
           throw new UserNotFoundException("User with specified email doesn't exist!");
       }

       return removedUser;
    }

    /**
     * Returns a collection of all users.
     * @return users - Iterable containing users
     */

    public Iterable<User> findAll() {
        return usersRepository.findAll();
    }

    /**
     * Returns the user corresponding to the specified ID.
     * @param userID - id of user
     * @return user - user with specified ID
     * @throws UserNotFoundException if the user was not found
     */

    public User findOne(Long userID){
        User user = usersRepository.findOne(userID);

        if(user == null){
            throw new UserNotFoundException("User with specified ID doesn't exist!");
        }

        return user;
    }

    /**
     * Returns the ID of the user that has the given email address.
     * @param email - email of user
     * @return ID - the corresponding user id
     * @throws UserNotFoundException if the user with the given email does not exist
     */

    public Long findIDByUserEmail(String email){
        Iterable<User> users = usersRepository.findAll();

        for(User user : users){
            if(user.getEmail().equals(email)){
                return user.getID();
            }
        }

        throw new UserNotFoundException("User with specified email does not exist!");
    }

    public User findUserByEmail(String email){
        User user = usersRepository.findUserByEmail(email);

        if(user == null){
            throw new UserNotFoundException("User with specified email does not exist");
        }

        return user;
    }

    public List<User> getUserListFromEmailsString(String userEmails){
        List<User> users = new ArrayList<>();

        for(String email : userEmails.split(" ")){
            try{
                User user = findUserByEmail(email);
                users.add(user);
            } catch (UserNotFoundException e){
                throw new UserNotFoundException("An email from the given list is invalid! Couldn't send message to users!");
            }
        }

        return users;
    }
}
