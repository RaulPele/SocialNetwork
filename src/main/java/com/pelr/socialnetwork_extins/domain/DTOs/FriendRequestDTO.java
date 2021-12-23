package com.pelr.socialnetwork_extins.domain.DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FriendRequestDTO
{
    private String firstName;
    private String lastName;
    private String email;
    private String status;
    private LocalDateTime date;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FriendRequestDTO(String firstName, String lastName, String email, String status, LocalDateTime date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Friend request from:\n" +
                "First name: " + firstName + '\n' +
                "Last name: " + lastName + '\n' +
                "Email: " + email + '\n' +
                "Status: " + status + '\n';
    }
}
