package com.pelr.socialnetwork_extins.repository.memory;

import com.pelr.socialnetwork_extins.domain.Entity;
import com.pelr.socialnetwork_extins.domain.validators.Validator;
import com.pelr.socialnetwork_extins.repository.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * InMemoryRepository generic class for im memory storage.
 * Implements Repository interface.
 * @param <ID> - ID of stored objects
 * @param <E> - type of stored objects, must extend Entity
 */

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private Map<ID, E> entities;
    private Validator<E> validator;

    /**
     * Creates InMemoryRepository object containing a validator.
     * @param validator - entity validator
     */

    public InMemoryRepository(Validator<E> validator){
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public E save(E entity) {
        if(entity == null){
            throw new IllegalArgumentException("Entity must not be null!");
        }

        validator.validate(entity);

        //check if the entity already exists
        if(entities.get(entity.getID()) != null){
            return entity;
        }

        entities.put(entity.getID(), entity);

        return null;
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E findOne(ID id) {
        if (id == null){
            throw new IllegalArgumentException("ID must not be null!");
        }

        return entities.get(id);
    }

    @Override
    public E remove(ID id) {
        if(id == null){
            throw new IllegalArgumentException("ID must not be null!");
        }

        return entities.remove(id);
    }

    @Override
    public E update(E entity) {
        return null;
    }
}
