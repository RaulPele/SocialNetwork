package com.pelr.socialnetwork_extins.repository.file;

import com.pelr.socialnetwork_extins.domain.User;
import com.pelr.socialnetwork_extins.domain.validators.Validator;

import java.util.List;

/**
 * UserFileRepository class
 */
public class UserFileRepository extends AbstractFileRepository<Long, User> {

    /**
     * Creates a userfilerepository that uses a file and a user validaor.
     *
     * @param fileName - name string of file
     * @param validator - user validator
     */
    public UserFileRepository(String fileName, Validator<User> validator) {
        super(fileName, validator);
    }

    /**
     * Returns corresponding string of user.
     *
     * @param entity - User object
     * @return corresponding string of user
     */
    @Override
    protected String createEntityAsString(User entity) {
        return entity.getID() + ";" + entity.getFirstName() + ";" + entity.getLastName() + ";" + entity.getEmail();
    }

    /**
     * Returns a user corresponding to a list of attributes
     *
     * @param attributes - list of attributes
     * @return user - corresponding user
     */
    @Override
    protected User extractEntity(List<String> attributes) {
        User user = new User(attributes.get(1), attributes.get(2), attributes.get(3));
        user.setID(Long.parseLong(attributes.get(0)));

        return user;
    }

    @Override
    public User update(User entity) {
        return null;
    }
}
