package com.pelr.socialnetwork_extins.service;

import com.pelr.socialnetwork_extins.domain.Friendship;
import com.pelr.socialnetwork_extins.domain.Message;
import com.pelr.socialnetwork_extins.domain.User;
import com.pelr.socialnetwork_extins.repository.Repository;
import com.pelr.socialnetwork_extins.repository.database.MessageDBRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public Message getLastMessageBetween(Long userId1, Long userId2) {
        return messagesRepository.getLastMessageBetween(userId1, userId2);
    }

    public List<Message> getMessagesReceivedBetween(Long userID, LocalDateTime startDate, LocalDateTime endDate) {
        return messagesRepository.getMessagesReceivedBetween(userID, startDate, endDate);
    }

    public List<Message> getMessagesReceivedFrom(Long userID, Long senderID, LocalDateTime startDate, LocalDateTime endDate) {
        Iterable<Message> allMessages = getMessagesSortedByDateBetween(userID, senderID);
        List<Message> receivedMessages = StreamSupport.stream(allMessages.spliterator(), false)
                .filter(message -> message.getFrom().getID().equals( senderID))
                .filter(message -> message.getDate().isAfter(startDate) || message.getDate().equals(startDate))
                .filter(message -> message.getDate().isBefore(endDate) || message.getDate().equals(endDate))
                .collect(Collectors.toList());

        return receivedMessages;
    }
}
