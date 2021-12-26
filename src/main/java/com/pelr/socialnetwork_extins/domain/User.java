package com.pelr.socialnetwork_extins.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * User entity class
 */
public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private String email;
    private UserAuthCredentials authCredentials;

    /**
     * Creates a user entity
     *
     * @param firstName - first name string of user
     * @param lastName  - last name string of user
     */
    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserAuthCredentials getAuthCredentials() {
        return authCredentials;
    }

    public void setAuthCredentials(UserAuthCredentials authCredentials) {
        this.authCredentials = authCredentials;
    }

    @Override
    public String toString() {
        return "User:\n" +
                "First name: '" + firstName + "'\n" +
                "Last name: '" + lastName + "'\n" +
                "Email: " + email + "\n";
    }
}
