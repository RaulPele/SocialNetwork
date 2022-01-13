package com.pelr.socialnetwork_extins.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Event extends Entity<Long>{
    private User creator;
    private String title;
    private String description;
    private List<User> participants;
    private LocalDateTime date;
    private String location;

    public Event(User creator, String title, String description, String location, LocalDateTime date) {
        this.creator = creator;
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.participants = new ArrayList<>();
    }

    public User getCreator() {
        return creator;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Event: \n" +
                "creator: " + creator.getFirstName() + " "+ creator.getLastName()  + "\n"+
                ", title: " + title + '\n' +
                ", description: " + description + '\n' +
                ", participants: " + participants.toString() + '\n' +
                ", date: " + date +'\n' +
                ", location: " + location + '\n';
    }

    public boolean isAttending(User user) {
        for(User participant : participants) {
            if(participant.getEmail().equals(user.getEmail())){
                return true;
            }
        }

        return false;
    }

    public void addParticipant(User user) {
        participants.add(user);
    }

    public void removeParticipant(User user) {
        participants.removeIf(participant -> participant.getEmail().equals(user.getEmail()));
    }
}
