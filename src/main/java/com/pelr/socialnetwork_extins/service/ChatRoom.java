package com.pelr.socialnetwork_extins.service;

import com.pelr.socialnetwork_extins.domain.Message;
import com.pelr.socialnetwork_extins.domain.User;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {
    private MessagingService messagingService;
    private UserService userService;
    private User sender;
    private List<User> receivers;
    private List<Message> messages;

    public ChatRoom(MessagingService messagingService, UserService userService, User sender, User receiver){
        this.messagingService = messagingService;
        this.userService = userService;
        this.sender = sender;
        this.receivers = new ArrayList<>();
        receivers.add(receiver);

        messages = null;

    }

    public void send(String message) {
        messagingService.save(sender, receivers, message, null);
    }

    public void reply(String message, long repliedToId){

        try {
            messagingService.save(sender, receivers, message, repliedToId);
        } catch (IndexOutOfBoundsException e) {
            throw new MessageNotFound("There is no message at the given index!");
        }
    }

    public void replyToAll(String message, long repliedToId){
        try {
            List<Long> receiversIds = messagingService.getAllReceiversOfMessage(repliedToId);
            List<User> actualReceivers  = new ArrayList<>();

            receiversIds.forEach(id -> {
                if(id != sender.getID()) {
                    actualReceivers.add(userService.findOne(id));
                }
            });

            //actualReceivers.add(receivers.get(0));
            messagingService.save(sender, actualReceivers, message, repliedToId);
        } catch (IndexOutOfBoundsException e) {
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
        initializeMessages();

        return messages;
    }

    public int getIndexOf(Message message){
        return messages.indexOf(message);
    }

    public void sendToMultipleUsers(String message, String userNames) {
        List<User> messageReceivers = userService.getUserListFromNamesString(userNames);
        messageReceivers.add(receivers.get(0));
        messagingService.save(sender, messageReceivers, message, null);
    }
}
