package com.pelr.socialnetwork_extins.repository;

import com.pelr.socialnetwork_extins.domain.Entity;
import com.pelr.socialnetwork_extins.domain.validators.ValidationException;

/**
 * Generic Repository interface class
 * @param <ID> - ID of stored objects
 * @param <E> - type of stored objects. Must extend Entity
 */

public interface Repository<ID,E extends Entity<ID>> {
    /**
     *
     * @param entity - entity to be saved, must not be null
     * @return null - if the entity is saved
     *  entity - if the entity already exists in the Repository
     * @throws ValidationException if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null
     */
    E save(E entity);

    /**
     * @return all entities
     */
    Iterable<E> findAll();


    /**
     * @param id - the id of the entity to be returned, id is not null
     * @return the entity with the specified id
     * or null if the entity doesn't exist
     * @throws IllegalArgumentException if id is null
     */
    E findOne(ID id);

    /**
     * Removes the entity with the specified id
     * @param id - ID, must not be null
     * @return null - if there is not entity with the specified ID in the repository
     *  entity- the removed Entity, otherwise
     * @throws IllegalArgumentException - if the given id is null
     */
    E remove(ID id);

    /**
     *
     * @param entity - entity must not be null
     * @return null - if the entity is updated,
     *                otherwise  returns the entity  - (e.g id does not exist).
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidationException if the entity is not valid.
     */
    E update(E entity);
}
