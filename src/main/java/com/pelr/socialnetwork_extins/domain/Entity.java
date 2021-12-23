package com.pelr.socialnetwork_extins.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Generic Entity class used by application entities.
 * @param <ID> - type of id of the entity
 */

public class Entity<ID> implements Serializable {
    //private static final long serialVersionUID = 8320620122267549434L;

    protected ID id;

    /**
     * Returns ID of entity
     * @return - entity id
     */

    public ID getID(){
        return id;
    }

    /**
     * Sets the ID of entity
     * @param newID - new entity id
     */

    public void setID(ID newID){
        this.id = newID;
    }

    /**
     * Equals function for entities
     * @param o - other entity
     * @return true if the entities are equal, false otherwise.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Entity<?> entity = (Entity<?>) o;

        return Objects.equals(id, entity.id);
    }

    /**
     * Returns hashCode of object.
     * @return hash - int representing hash
     */

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

//alt comenatriu de proba