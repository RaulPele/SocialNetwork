package com.pelr.socialnetwork_extins.repository.database;

import com.pelr.socialnetwork_extins.domain.Message;
import com.pelr.socialnetwork_extins.domain.User;
import com.pelr.socialnetwork_extins.repository.Repository;
import com.pelr.socialnetwork_extins.repository.RepositoryException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDBRepository implements Repository<Long, Message> {
    private String url;
    private String username;
    private String password;

    public MessageDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Message save(Message entity) {
        if(entity == null){
            throw new IllegalArgumentException("Entity must not be null!");
        }

        String saveMessageRepliedNullSql = "INSERT INTO \"Messages\"(from_user_id, message, date) VALUES (?, ?, ?)";
        String saveMessageRepliedNotNullSql = "INSERT INTO \"Messages\"(from_user_id, message, date, replied_to_id) VALUES (?, ?, ?, ?)";
        String sendMessageSql = "INSERT INTO \"SentMessages\"(message_id, to_user_id) VALUES (?, ?)";
        String getLastIdSql = "SELECT id FROM \"Messages\" ORDER BY id DESC limit 1";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement sendStatement = connection.prepareStatement(sendMessageSql);
            PreparedStatement getIDStatement = connection.prepareStatement(getLastIdSql)) {

            PreparedStatement saveStatement;
            long lastID;

            if(entity.getRepliedTo() == null){
                saveStatement = connection.prepareStatement(saveMessageRepliedNullSql);

                saveStatement.setLong(1, entity.getFrom().getID());
                saveStatement.setString(2, entity.getMessage());
                saveStatement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));

                saveStatement.executeUpdate();

                ResultSet lastMessage =  getIDStatement.executeQuery();
                lastMessage.next();

                lastID = lastMessage.getLong("id");

                for(User receiver : entity.getTo()){
                    sendStatement.setLong(1, lastID);
                    sendStatement.setLong(2, receiver.getID());

                    sendStatement.executeUpdate();
                    lastMessage.close();
                }
            } else {
                saveStatement = connection.prepareStatement(saveMessageRepliedNotNullSql);

                saveStatement.setLong(1, entity.getFrom().getID());
                saveStatement.setString(2, entity.getMessage());
                saveStatement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
                saveStatement.setLong(4, entity.getRepliedTo().getID());

                saveStatement.executeUpdate();

                ResultSet lastMessage =  getIDStatement.executeQuery();
                lastMessage.next();

                lastID = lastMessage.getLong("id");

                for(User receiver : entity.getTo()){
                    sendStatement.setLong(1, lastID);
                    sendStatement.setLong(2, receiver.getID());

                    sendStatement.executeUpdate();
                    lastMessage.close();
                }
            }

            saveStatement.close();
        } catch  (SQLException e){
            throw new RepositoryException("Messages table database save exception!\n" + e.getMessage());
        }

        return null;
    }

    private Message getMessageFromResultSet(ResultSet resultSet){
        Message message;

        try {
            Long messageId = resultSet.getLong("message_id");
            Long fromId = resultSet.getLong("sender_id");
            Long toId = resultSet.getLong("receiver_id");
            String messageString = resultSet.getString("message");
            LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
            Long repliedToId = resultSet.getLong("replied_to_id");

            String fromFirstName = resultSet.getString("sender_first_name");
            String fromLastName = resultSet.getString("sender_last_name");
            String fromEmail = resultSet.getString("sender_email");

            String toFirstName = resultSet.getString("receiver_first_name");
            String toLastName = resultSet.getString("receiver_last_name");
            String toEmail = resultSet.getString("receiver_email");

            User from = new User(fromFirstName, fromLastName, fromEmail);
            from.setID(fromId);

            User to = new User(toFirstName, toLastName, toEmail);
            to.setID(toId);

            ArrayList<User> receivers = new ArrayList<>();
            receivers.add(to);

            message = new Message(from, receivers, messageString);
            message.setDate(date);
            message.setID(messageId);

            if(repliedToId != 0){
                message.setRepliedTo(findOne(repliedToId));
            }
        }catch (SQLException e) {
            throw new RepositoryException("Messages table database findOne error!\n" + e.getMessage());
        }

        return message;
    }

    @Override
    public Iterable<Message> findAll() {
        String findAllSql = "select sentMessage.id as message_id, m.from_user_id as sender_id,\n" +
                "sender.first_name as sender_first_name, sender.last_name as sender_last_name,\n" +
                "sender.email as sender_email,\n" +
                "sentMessage.to_user_id as receiver_id, receiver.first_name as receiver_first_name, receiver.last_name as receiver_last_name,\n" +
                "receiver.email as receiver_email,\n" +
                "m.message, m.replied_to_id, m.date\n" +
                "from \"SentMessages\" sentMessage\n" +
                "inner join \"Messages\" m on m.id = sentMessage.message_id\n" +
                "inner join \"Users\" sender on sender.id = m.from_user_id\n" +
                "inner join \"Users\" receiver on receiver.id = sentMessage.to_user_id";

        List<Message> messages;

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement findStatement = connection.prepareStatement(findAllSql);
            ResultSet resultSet = findStatement.executeQuery()){

            messages = new ArrayList<>();

            while(resultSet.next()){
                messages.add(getMessageFromResultSet(resultSet));
            }
        } catch  (SQLException e) {
            throw new RepositoryException("Messages table database findall exception!\n" + e.getMessage());
        }

        return messages;
    }

    @Override
    public Message findOne(Long messageId) {
        if(messageId == null){
            throw new IllegalArgumentException("Id must not be null!");
        }

        Message message = null;
        String findSql = "select sentMessage.id as message_id, m.from_user_id as sender_id,\n" +
                "sender.first_name as sender_first_name, sender.last_name as sender_last_name,\n" +
                "sender.email as sender_email,\n" +
                "sentMessage.to_user_id as receiver_id, receiver.first_name as receiver_first_name, receiver.last_name as receiver_last_name,\n" +
                "receiver.email as receiver_email,\n" +
                "m.message, m.replied_to_id, m.date\n" +
                "from \"SentMessages\" sentMessage\n" +
                "inner join \"Messages\" m on m.id = sentMessage.message_id\n" +
                "inner join \"Users\" sender on sender.id = m.from_user_id\n" +
                "inner join \"Users\" receiver on receiver.id = sentMessage.to_user_id\n" +
                "where sentMessage.id = ?";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement findStatement = connection.prepareStatement(findSql)){

            findStatement.setLong(1, messageId);
            ResultSet resultSet = findStatement.executeQuery();

            if(resultSet.next()){
                message = getMessageFromResultSet(resultSet);
            }

            resultSet.close();
        } catch (SQLException e) {
            throw new RepositoryException("Messages table database findOne error!\n" + e.getMessage());
        }

        return message;
    }

    public Iterable<Message> getMessagesSortedByDateBetween(Long userId1, Long userId2){
        List<Message> messages = new ArrayList<>();
        String findSql = "SELECT sentMessage.id AS message_id, m.from_user_id AS sender_id,\n" +
                "sender.first_name AS sender_first_name, sender.last_name AS sender_last_name,\n" +
                "sender.email AS sender_email,\n" +
                "sentMessage.to_user_id AS receiver_id, receiver.first_name AS receiver_first_name, receiver.last_name AS receiver_last_name,\n" +
                "receiver.email AS receiver_email,\n" +
                "m.message, m.replied_to_id, m.date\n" +
                "FROM \"SentMessages\" sentMessage\n" +
                "INNER JOIN \"Messages\" m on m.id = sentMessage.message_id\n" +
                "INNER JOIN \"Users\" sender on sender.id = m.from_user_id\n" +
                "INNER JOIN \"Users\" receiver on receiver.id = sentMessage.to_user_id\n" +
                "WHERE ((sentMessage.to_user_id = ? and m.from_user_id = ?) or (sentMessage.to_user_id = ? and m.from_user_id = ?))\n"+
                "ORDER BY date ASC;";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement findStatement = connection.prepareStatement(findSql)){

            findStatement.setLong(1, userId1);
            findStatement.setLong(2, userId2);
            findStatement.setLong(3, userId2);
            findStatement.setLong(4, userId1);

            ResultSet resultSet = findStatement.executeQuery();

            while(resultSet.next()){
                messages.add(getMessageFromResultSet(resultSet));
            }
        } catch (SQLException e){
            throw new RepositoryException("Message table database get sorted messages between users error!\n"+e.getMessage());
        }

        return messages;
    }

    public Message getLastMessageBetween(Long userId1, Long userId2){
        Message message = null;

        String findSql = "SELECT sentMessage.id AS message_id, m.from_user_id AS sender_id,\n" +
                "sender.first_name AS sender_first_name, sender.last_name AS sender_last_name,\n" +
                "sender.email AS sender_email,\n" +
                "sentMessage.to_user_id AS receiver_id, receiver.first_name AS receiver_first_name, receiver.last_name AS receiver_last_name,\n" +
                "receiver.email AS receiver_email,\n" +
                "m.message, m.replied_to_id, m.date\n" +
                "FROM \"SentMessages\" sentMessage\n" +
                "INNER JOIN \"Messages\" m on m.id = sentMessage.message_id\n" +
                "INNER JOIN \"Users\" sender on sender.id = m.from_user_id\n" +
                "INNER JOIN \"Users\" receiver on receiver.id = sentMessage.to_user_id\n" +
                "WHERE ((sentMessage.to_user_id = ? and m.from_user_id = ?) or (sentMessage.to_user_id = ? and m.from_user_id = ?))\n"+
                "ORDER BY date DESC LIMIT 1;";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement findStatement = connection.prepareStatement(findSql)){

            findStatement.setLong(1, userId1);
            findStatement.setLong(2, userId2);
            findStatement.setLong(3, userId2);
            findStatement.setLong(4, userId1);

            ResultSet resultSet = findStatement.executeQuery();

            if(resultSet.next()){
                return getMessageFromResultSet(resultSet);
            }
        } catch (SQLException e){
            throw new RepositoryException("Message table database get sorted messages between users error!\n"+e.getMessage());
        }

        return message;
    }

    public List<Long> getAllReceiversOfMessage(Long messageID){
        String getReceiversSql = "SELECT DISTINCT to_user_id FROM \"SentMessages\" WHERE message_id = (SELECT message_id FROM \"SentMessages\" where id = ?)  ";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement getReceiversStatement = connection.prepareStatement(getReceiversSql)) {

            getReceiversStatement.setLong(1, messageID);
            ResultSet resultSet = getReceiversStatement.executeQuery();

            List<Long> receiversIDs = new ArrayList<>();

            while(resultSet.next()){
                long id = resultSet.getLong("to_user_id");
                receiversIDs.add(id);
            }
            resultSet.close();

            return receiversIDs;
        } catch (SQLException e) {
            throw new RepositoryException("Message table database getAllReceiversOfMessage error!\n" + e.getMessage());
        }

    }

    @Override
    public Message remove(Long aLong) {
        return null;
    }

    @Override
    public Message update(Message entity) {
        return null;
    }

    public List<Message> getMessagesReceivedBetween(Long userID, LocalDateTime startTime, LocalDateTime endTime) {
        String getSql = "SELECT sentMessage.id AS message_id, m.from_user_id AS sender_id,\n" +
                "sender.first_name AS sender_first_name, sender.last_name AS sender_last_name,\n" +
                "sender.email AS sender_email,\n" +
                "sentMessage.to_user_id AS receiver_id, receiver.first_name AS receiver_first_name, receiver.last_name AS receiver_last_name,\n" +
                "receiver.email AS receiver_email,\n" +
                "m.message, m.replied_to_id, m.date\n" +
                "FROM \"SentMessages\" sentMessage\n" +
                "INNER JOIN \"Messages\" m on m.id = sentMessage.message_id\n" +
                "INNER JOIN \"Users\" sender on sender.id = m.from_user_id\n" +
                "INNER JOIN \"Users\" receiver on receiver.id = sentMessage.to_user_id\n" +
                "WHERE sentMessage.to_user_id = ? AND m.date BETWEEN ? and ?";

        List<Message> messages = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement getStatement = connection.prepareStatement(getSql)) {

            getStatement.setLong(1, userID);
            getStatement.setTimestamp(2, Timestamp.valueOf(startTime));
            getStatement.setTimestamp(3, Timestamp.valueOf(endTime));

            ResultSet resultSet = getStatement.executeQuery();

            while(resultSet.next()){
                messages.add(getMessageFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Get messages received between database error!\n" + e.getMessage());
        }

        return messages;
    }
}

