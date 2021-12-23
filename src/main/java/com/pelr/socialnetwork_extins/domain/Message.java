package com.pelr.socialnetwork_extins.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Message extends Entity<Long>{
    private User from;
    private List<User> to;
    private String message;
    private Message repliedTo;
    private LocalDateTime date;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Message(User from, List<User> to, String message){
        this.from = from;
        this.to = to;
        this.message = message;
        this.repliedTo = null;
        this.date = LocalDateTime.now();
    }


    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public List<User> getTo() {
        return to;
    }

    public void setTo(List<User> to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message getRepliedTo() {
        return repliedTo;
    }

    public void setRepliedTo(Message repliedTo) {
        this.repliedTo = repliedTo;
    }

    private String getReceiversString(){
        StringBuilder receivers =  new StringBuilder();

        to.forEach(receiver -> receivers.append(receiver.getFirstName() + " " + receiver.getLastName() + ", "));

        receivers.delete(receivers.length()-2, receivers.length());

        return receivers.toString();
    }

    @Override
    public String toString() {
        String messageString;
        if(repliedTo != null){
            messageString = "From: " + from.getFirstName() +" " + from.getLastName() + "\n" +
                    "To: " + getReceiversString() + "\n" +
                    "Message:\n" + message + '\n' +
                    "Replied to: " + repliedTo.getMessage() + "\n" +
                    "Date: " + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else {
            messageString = "From: " + from.getFirstName() +" " + from.getLastName() + "\n" +
                    "To: " + getReceiversString() + "\n" +
                    "Message:\n" + message + '\n' +
                    "Date: " + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        return messageString;
    }
}
