package com.pelr.socialnetwork_extins.domain.validators;

/**
 * Generic Validator interface class
 * @param <T> - type of Validator
 */

public interface Validator<T>{
    /**
     * Validates specific entity.
     * @param entity - entity object
     * @throws ValidationException - if the given entity is invalid
     */

    void validate(T entity) throws ValidationException;
}
