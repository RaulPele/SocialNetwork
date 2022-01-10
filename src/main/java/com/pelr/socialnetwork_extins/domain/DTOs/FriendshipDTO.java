package com.pelr.socialnetwork_extins.domain.DTOs;

import java.time.LocalDateTime;

/**
 * FriendshipDTO entity class
 */
public class FriendshipDTO {
    private String firstName;
    private String lastName;
    private String date;
    private String email;

    /**
     * create a FriendshippDTO entity
     */
    public FriendshipDTO(String firstName, String lastName, String date, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return a string with the parameters
     */
    @Override
    public String toString() {
        return "First name : " + firstName + "\n" + "Last name: " + lastName + "\n" + "Date: " + date + "\n" ;

    }
}
