package com.pelr.socialnetwork_extins.repository.database;

import com.pelr.socialnetwork_extins.domain.User;
import com.pelr.socialnetwork_extins.domain.validators.Validator;
import com.pelr.socialnetwork_extins.repository.Repository;
import com.pelr.socialnetwork_extins.repository.RepositoryException;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDBRepository implements Repository<Long, User> {
    private String url;
    private String username;
    private String password;

    private Validator<User> validator;

    public UserDBRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public User save(User entity) {
        if(entity == null){
            throw new IllegalArgumentException("Entity must not be null!");
        }

        validator.validate(entity);

        String addSql = "INSERT INTO \"Users\"(first_name, last_name, email, password) VALUES (?, ?, ?, ?)";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement addStatement = connection.prepareStatement(addSql)){

            addStatement.setString( 1, entity.getFirstName());
            addStatement.setString(2, entity.getLastName());
            addStatement.setString(3, entity.getEmail());
            addStatement.setString(4, entity.getAuthCredentials().getPassword());

            addStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("User database table exception!\n" + e.getMessage());
        }

        return null;
    }

    @Override
    public Iterable<User> findAll() {
        String findAllSql = "SELECT * FROM \"Users\"";
        Set<User> users = new HashSet<>();

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement findStatement = connection.prepareStatement(findAllSql);
            ResultSet resultSet = findStatement.executeQuery()){

            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");

                User user = new User(firstName, lastName, email);
                user.setID(id);

                users.add(user);
            }
        }catch(SQLException e){
            throw new RepositoryException("User database 'find all' exception!\n" + e.getMessage());
        }

        return users;
    }

    @Override
    public User findOne(Long id) {
        if(id == null){
            throw new IllegalArgumentException("ID must not be null!");
        }

        String findSql = "SELECT * FROM \"Users\" WHERE id = ?";
        User user = null;

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement findStatement = connection.prepareStatement(findSql)){

            findStatement.setLong(1, id);
            ResultSet resultSet = findStatement.executeQuery();

            if(resultSet.next()){
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");

                user = new User(firstName, lastName, email);
                user.setID(id);
            }

            resultSet.close();
        } catch(SQLException e) {
            throw new RepositoryException("User database 'find one' exception!\n" + e.getMessage());
        }

        return user;
    }

    @Override
    public User remove(Long id) {
        if(id == null){
            throw new IllegalArgumentException("ID must not be null!");
        }

        User removedUser = findOne(id);
        if(removedUser == null){
            return null;
        }

        String removeSql = "DELETE FROM \"Users\" WHERE id = ?";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement removeStatement = connection.prepareStatement(removeSql)){

            removeStatement.setLong(1, id);
            removeStatement.executeUpdate();
        }catch (SQLException e){
            throw new RepositoryException("User database remove exception!\n" + e.getMessage());
        }

        return removedUser;
    }

    @Override
    public User update(User entity) {
        if(entity == null){
            throw new IllegalArgumentException("Entity must not be null!");
        }

        validator.validate(entity);

        if(findOne(entity.getID()) == null){
            return entity;
        }

        String updateSql = "UPDATE \"Users\" SET first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement updateStatement = connection.prepareStatement(updateSql)){

            updateStatement.setString(1, entity.getFirstName());
            updateStatement.setString(2, entity.getLastName());
            updateStatement.setString(3, entity.getEmail());
            updateStatement.setString(4, entity.getAuthCredentials().getPassword());
            updateStatement.setLong(5, entity.getID());

            updateStatement.executeUpdate();
        } catch(SQLException e){
            throw new RepositoryException("User database update exception!\n" + e.getMessage());
        }

        return null;
    }

    public User findUserByEmail(String email){
        String findSql = "SELECT * FROM \"Users\" WHERE email = ?";
        User user = null;

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement findStatement = connection.prepareStatement(findSql)){

            findStatement.setString(1, email);
            ResultSet resultSet = findStatement.executeQuery();

            if(resultSet.next()){
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                user = new User(firstName, lastName, email);
                user.setID(id);
            }
        } catch  (SQLException e) {
            throw new RepositoryException("User database find user by email exception!\n" + e.getMessage());
        }

        return user;
    }

    public String getUserPasswordHash(String email) {
        String getPasswordSql = "SELECT password FROM \"Users\" WHERE email = ?";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement getPasswordStatement = connection.prepareStatement(getPasswordSql)){

            getPasswordStatement.setString(1, email);
            ResultSet resultSet = getPasswordStatement.executeQuery();
            resultSet.next();

            return resultSet.getString("password");
        } catch (SQLException e) {
            throw new RepositoryException("User database get password hash error!\n" + e.getMessage());
        }
    }

    public User findUserByName(String firstName, String lastName) {
        String findSql = "SELECT * FROM \"Users\" WHERE first_name = ? AND last_name = ?";
        User user = null;

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement findStatement = connection.prepareStatement(findSql)){

            findStatement.setString(1, firstName);
            findStatement.setString(2, lastName);

            ResultSet resultSet = findStatement.executeQuery();

            if(resultSet.next()){
                Long id = resultSet.getLong("id");
                String email = resultSet.getString("email");

                user = new User(firstName, lastName, email);
                user.setID(id);
            }
        }catch  (SQLException e){
            throw new RepositoryException("User database find user by email exception!\n" + e.getMessage());
        }

        return user;
    }
}
