package com.pelr.socialnetwork_extins.service;

import com.pelr.socialnetwork_extins.domain.Message;
import com.pelr.socialnetwork_extins.domain.User;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {
    private MessagingService messagingService;
    private User sender;
    private List<User> receivers;
    private List<Message> messages;

    public ChatRoom(MessagingService messagingService, User sender, User receiver){
        this.messagingService = messagingService;
        this.sender = sender;
        this.receivers = new ArrayList<>();
        receivers.add(receiver);

        messages = null;

    }

    public void send(String message) {
        messagingService.save(sender, receivers, message, null);
    }

    public void reply(String message, int replyToIndex){
        long repliedToId = 0;

        try{
            repliedToId = messages.get(replyToIndex).getID();
            messagingService.save(sender, receivers, message, repliedToId);
        }catch (IndexOutOfBoundsException e){
            throw new MessageNotFound("There is no message at the given index!");
        }
    }

    public User getReceiver(){
        return this.receivers.get(0);
    }

    public void initializeMessages(){
        messages = new ArrayList<>();

        Iterable<Message> messageIterable = messagingService.getMessagesSortedByDateBetween(sender.getID(), getReceiver().getID());
        messageIterable.forEach(message -> messages.add(message));
    }

    public List<Message> getMessages(){
        if(messages == null){
            initializeMessages();
        }

        return messages;
    }

    public int getIndexOf(Message message){
        return messages.indexOf(message);
    }
}
