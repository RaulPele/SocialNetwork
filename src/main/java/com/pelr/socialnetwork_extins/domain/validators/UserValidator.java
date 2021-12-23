package com.pelr.socialnetwork_extins.domain.validators;

import com.pelr.socialnetwork_extins.domain.User;

/**
 * UserValidator class
 */
public class UserValidator implements  Validator<User> {

    /**
     * Validates a name string
     *
     * @param name - name string
     * @return true - if the name is valid;
     * false - if the name is invalid;
     */
    private boolean validateName(String name) {
        if (name.isEmpty() || name.isBlank()) {
            return false;
        }

        for (int i = 0; i < name.length(); i++) {
            if (!Character.isAlphabetic(name.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty() || email.isBlank()) {
            return false;
        }

        if (!Character.isAlphabetic(email.charAt(0))) {
            return false;
        }

        if (!email.contains("@")) {
            return false;
        }

        return true;
    }

    /**
     * Validates a user entity
     *
     * @param entity - user entity
     * @throws ValidationException - if user is invalid
     */
    @Override
    public void validate(User entity) throws ValidationException {
        StringBuilder exceptionMsg = new StringBuilder();

        if (!validateName(entity.getFirstName())) {
            exceptionMsg.append("Invalid first name!\n");
        }

        if (!validateName(entity.getLastName())) {
            exceptionMsg.append("Invalid last name!\n");
        }

        if (!validateEmail(entity.getEmail())) {
            exceptionMsg.append("Invalid email!\n");
        }

        if (!exceptionMsg.isEmpty()) {
            throw new ValidationException(exceptionMsg.toString());
        }
    }
}
