package com.pelr.socialnetwork_extins.domain.DTOs;

public class ConversationHeaderDTO {
    private String receiverFirstName;
    private String receiverLastName;
    private String receiverEmail;

    public ConversationHeaderDTO(String receiverFirstName, String receiverLastName, String receiverEmail) {
        this.receiverFirstName = receiverFirstName;
        this.receiverLastName = receiverLastName;
        this.receiverEmail = receiverEmail;
    }

    public String getReceiverFirstName() {
        return receiverFirstName;
    }

    public String getReceiverLastName() {
        return receiverLastName;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    @Override
    public String toString() {
        return receiverFirstName + " " + receiverLastName + ": " + receiverEmail;
    }
}
