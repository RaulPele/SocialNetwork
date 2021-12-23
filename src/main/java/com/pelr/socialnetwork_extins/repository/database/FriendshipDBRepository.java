package com.pelr.socialnetwork_extins.repository.database;

import com.pelr.socialnetwork_extins.domain.Friendship;
import com.pelr.socialnetwork_extins.domain.Status;
import com.pelr.socialnetwork_extins.domain.Tuple;
import com.pelr.socialnetwork_extins.domain.validators.Validator;
import com.pelr.socialnetwork_extins.repository.Repository;
import com.pelr.socialnetwork_extins.repository.RepositoryException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class FriendshipDBRepository implements Repository<Tuple<Long, Long>, Friendship> {

    private String url;
    private String username;
    private String password;

    private Validator<Friendship> validator;

    public FriendshipDBRepository(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Friendship save(Friendship entity) {
        if(entity == null){
            throw new IllegalArgumentException("Entity must not be null!");
        }
        Friendship mirroredFriendship = new Friendship();

        if(findOne(entity.getID()) != null ){
            return entity;
        }

        String addSql = "INSERT INTO \"Friendships\"(user_id1, user_id2, date, status) VALUES(?, ?, ?, ?)";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement addStatement = connection.prepareStatement(addSql)){

            addStatement.setLong(1, entity.getID().getLeft());
            addStatement.setLong(2, entity.getID().getRight());
            addStatement.setTimestamp(3,Timestamp.valueOf(entity.getDate()));
            addStatement.setString(4,entity.getStatus().toString());

            addStatement.executeUpdate();
        } catch(SQLException e){
            throw new RepositoryException("Friendship database table exception!");
        }

        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        String findSql = "SELECT * FROM \"Friendships\"";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement findStatement = connection.prepareStatement(findSql);
            ResultSet resultSet = findStatement.executeQuery()){

            while(resultSet.next()){
                Tuple<Long, Long> id = new Tuple<>(resultSet.getLong("user_id1"), resultSet.getLong("user_id2"));
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                String status = resultSet.getString("status" );

                Friendship friendship = new Friendship();
                friendship.setID(id);
                friendship.setDate(date);

                switch (status)
                {
                    case "APPROVED" -> friendship.setStatus(Status.APPROVED);
                    case "PENDING" -> friendship.setStatus(Status.PENDING);
                    case "REJECTED" -> friendship.setStatus(Status.REJECTED);
                }

                friendships.add(friendship);
            }
        } catch(SQLException e) {
            throw new RepositoryException("Friendship findAll table exception!");
        }

        return friendships;
    }

    @Override
    public Friendship findOne(Tuple<Long, Long> friendshipID) {
        Friendship friendship = null;
        String findSql = "SELECT * FROM \"Friendships\" WHERE user_id1 = ? AND user_id2 = ?";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement findStatement = connection.prepareStatement(findSql)){

            findStatement.setLong(1, friendshipID.getLeft());
            findStatement.setLong(2, friendshipID.getRight());

            ResultSet resultSet = findStatement.executeQuery();

            if(resultSet.next()){
                friendship = new Friendship();
                friendship.setID(friendshipID);
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                friendship.setDate(date);
                String status = resultSet.getString("status" );

                switch (status)
                {
                    case "APPROVED" -> friendship.setStatus(Status.APPROVED);
                    case "PENDING" -> friendship.setStatus(Status.PENDING);
                    case "REJECTED" -> friendship.setStatus(Status.REJECTED);
                }
            }

            resultSet.close();
        } catch (SQLException e){
            throw new RepositoryException("Friendship findOne table exception!");
        }

        return friendship;
    }

    @Override
    public Friendship remove(Tuple<Long, Long> friendshipID) {
        if(friendshipID == null) {
            throw new IllegalArgumentException("Id must not be null!");
        }

        Friendship removedFriendship = null;
        String removeSql = "DELETE FROM \"Friendships\" WHERE user_id1 = ? AND user_id2 = ?";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement removeStatement = connection.prepareStatement(removeSql)){

            removedFriendship= findOne(friendshipID);

            if(removedFriendship == null){
                Tuple<Long, Long> mirroredFriendshipID = new Tuple<>(friendshipID.getRight(), friendshipID.getLeft());
                removedFriendship = findOne(mirroredFriendshipID);

                if(removedFriendship == null){
                    return null;
                }
            }

            removeStatement.setLong(1, removedFriendship.getID().getLeft());
            removeStatement.setLong(2, removedFriendship.getID().getRight());

            removeStatement.executeUpdate();
        } catch(SQLException e){
            throw new RepositoryException("Friendship remove table exception!");
        }

        return removedFriendship;
    }

    @Override
    public Friendship update(Friendship entity) {
        if (entity == null) {
            throw  new IllegalArgumentException("Entity must not be null!");
        }

        String updateSql = "UPDATE \"Friendships\" SET  date = ?, status = ? " +
                "WHERE user_id1 =? and user_id2 = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement removeStatement = connection.prepareStatement(updateSql)) {

            removeStatement.setLong(3, entity.getID().getLeft());
            removeStatement.setLong(4, entity.getID().getRight());
            removeStatement.setTimestamp(1, Timestamp.valueOf(entity.getDate()));
            removeStatement.setString(2, entity.getStatus().toString());

            removeStatement.executeUpdate();
        } catch (SQLException e) {
           throw new RepositoryException("Friendship update table exception!");
       }

        return null;
    }
}
