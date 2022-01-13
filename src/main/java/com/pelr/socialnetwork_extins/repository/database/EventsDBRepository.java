package com.pelr.socialnetwork_extins.repository.database;

import com.pelr.socialnetwork_extins.domain.Event;
import com.pelr.socialnetwork_extins.domain.User;
import com.pelr.socialnetwork_extins.repository.Repository;
import com.pelr.socialnetwork_extins.repository.RepositoryException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventsDBRepository implements Repository<Long, Event> {
    private String url;
    private String username;
    private String password;

    public EventsDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Event save(Event entity) {
        if(entity == null){
            throw new IllegalArgumentException("Entity must not be null!");
        }

        String addSql = "INSERT INTO \"Events\"(creator_id, title, description, location, date) VALUES (?, ?, ?, ?, ?)";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement addStatement = connection.prepareStatement(addSql)) {

            addStatement.setLong(1, entity.getCreator().getID());
            addStatement.setString(2, entity.getTitle());
            addStatement.setString(3, entity.getDescription());
            addStatement.setString(4, entity.getLocation());
            addStatement.setTimestamp(5, Timestamp.valueOf(entity.getDate()));

            addStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException("Events database save error!\n" + e.getMessage());
        }

        return null;
    }

    private Event getEventFromResultSet(ResultSet resultSet) {
        try {
            String firstName = resultSet.getString("first_name");
            String last_name = resultSet.getString("last_name");
            String email = resultSet.getString("email");
            Long userId = resultSet.getLong("creator_id");
            Long eventId = resultSet.getLong("event_id");
            String title = resultSet.getString("title");
            String description = resultSet.getString("description");
            String location = resultSet.getString("location");
            LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

            User creator = new User(firstName, last_name, email);
            creator.setID(userId);

            Event event = new Event(creator, title, description ,location, date);
            List<User> participants = getEventParticipants(eventId);
            event.setParticipants(participants);
            event.setID(eventId);

            return event;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Iterable<Event> findAll() {
        String findAllSql = "SELECT e.id as event_id, e.title, e.description ,e.location, e.date," +
                "u.id as creator_id, u.first_name, u.last_name, u.email FROM \"Events\" e " +
                "INNER JOIN \"Users\" u on u.id = e.creator_id";

        List<Event> events = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement findStatement = connection.prepareStatement(findAllSql);
            ResultSet resultSet = findStatement.executeQuery()) {

            while(resultSet.next()) {
                events.add(getEventFromResultSet(resultSet));
            }

            return events;
        } catch(SQLException e) {
            throw new RepositoryException("Events database findall error!\n" + e.getMessage());
        }
    }

    @Override
    public Event findOne(Long eventID) {
        if(eventID == null) {
            throw new IllegalArgumentException("Id must not be null!");
        }

        String findOneSql = "SELECT e.id as event_id, e.title, e.description ,e.location, e.date," +
                "u.id as creator_id, u.first_name, u.last_name, u.email FROM \"Events\" e" +
                "INNER JOIN \"Users\" u on u.id = e.creator_id " +
                "WHERE e.id = ?";

        Event event = null;

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement findStatement = connection.prepareStatement(findOneSql)) {

            findStatement.setLong(1, eventID);
            ResultSet resultSet = findStatement.executeQuery();
            if(resultSet.next()) {
                event = getEventFromResultSet(resultSet);
            }

            resultSet.close();
        } catch (SQLException e) {
            throw new RepositoryException("Events database findOne error!\n" + e.getMessage());
        }

        return event;
    }

    @Override
    public Event remove(Long aLong) {
        return null;
    }

    @Override
    public Event update(Event entity) {
        return null;
    }

    public List<User> getEventParticipants(Long eventId) {
        String findParticipantsSql = "SELECT u.id, u.first_name, u.last_name, u.email FROM \"EventsAttendees\" ea " +
                                "INNER JOIN \"Users\" u on u.id = ea.user_id " +
                                "WHERE ea.event_id = ?";

        List<User> participants = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement findStatement = connection.prepareStatement(findParticipantsSql)) {

            findStatement.setLong(1, eventId);
            ResultSet resultSet = findStatement.executeQuery();

            while(resultSet.next()) {
                Long userId = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");

                User attendee = new User(firstName, lastName, email);
                attendee.setID(userId);

                participants.add(attendee);
            }

        } catch (SQLException e) {
            throw new RepositoryException("Events get participants database error!\n" + e.getMessage());
        }

        return participants;
    }

    public void attendToEvent(Long userID, Long eventID) {
        String attendSql = "INSERT INTO \"EventsAttendees\"(user_id, event_id) VALUES (?, ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement attendStatement = connection.prepareStatement(attendSql)) {

            attendStatement.setLong(1, userID);
            attendStatement.setLong(2, eventID);

            attendStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Attend to event database error!\n" + e.getMessage());
        }
    }

    public void cancelAttendingToEvent(Long userID, Long eventID) {
        String cancelSql = "DELETE FROM \"EventsAttendees\" WHERE user_id = ? AND event_id = ?";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement cancelStatement = connection.prepareStatement(cancelSql)) {

            cancelStatement.setLong(1, userID);
            cancelStatement.setLong(2, eventID);

            cancelStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RepositoryException("Cancel attending to event database error!\n" + e.getMessage());
        }
    }
}
