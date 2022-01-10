package com.pelr.socialnetwork_extins.service;

import com.pelr.socialnetwork_extins.domain.Friendship;
import com.pelr.socialnetwork_extins.domain.Message;
import com.pelr.socialnetwork_extins.domain.User;
import com.pelr.socialnetwork_extins.repository.Repository;
import com.pelr.socialnetwork_extins.repository.database.MessageDBRepository;

import java.util.List;

public class MessagingService {
    private MessageDBRepository messagesRepository;

    public MessagingService(MessageDBRepository messagesRepository){
        this.messagesRepository = messagesRepository;
    }

    public Message save(User from, List<User> to, String messageContent, Long repliedToId){
        Message message = new Message(from, to, messageContent);

        if(repliedToId != null){
            message.setRepliedTo(messagesRepository.findOne(repliedToId));
        }

        return messagesRepository.save(message);
    }

    public Message findOne(Long messageId){
        return messagesRepository.findOne(messageId);
    }

    public Iterable<Message> findAll(){
        return messagesRepository.findAll();
    }

    public Iterable<Message> getMessagesSortedByDateBetween(Long userId1, Long userId2){
        return messagesRepository.getMessagesSortedByDateBetween(userId1, userId2);
    }

    public List<Long> getAllReceiversOfMessage(Long messageID) {
        return messagesRepository.getAllReceiversOfMessage(messageID);
    }
}
