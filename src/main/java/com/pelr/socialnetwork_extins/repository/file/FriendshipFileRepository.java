package com.pelr.socialnetwork_extins.repository.file;

import com.pelr.socialnetwork_extins.domain.Friendship;
import com.pelr.socialnetwork_extins.domain.Tuple;
import com.pelr.socialnetwork_extins.domain.validators.Validator;

import java.util.List;

/**
 * FriendshipFileRepository class
 */

public class FriendshipFileRepository extends AbstractFileRepository<Tuple<Long, Long>, Friendship> {

    /**
     * Creates a friendshipFileRepository that uses a file and a friendship validator.
     * @param fileName - name string of file
     * @param validator - friendship validator
     */
    public FriendshipFileRepository(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
    }

    /**
     * Return string of  friendship
     * @param entity - Friendship object
     * @return string of friendship
     */
    @Override
    protected String createEntityAsString(Friendship entity) {
        //Friendship: userID1;userID2
        Tuple<Long, Long> idTuple = entity.getID();

        return idTuple.getLeft() + ";" + idTuple.getRight();
    }

    /** Returns a friendship entity created from a list of attributes.
     * @param attributes - list of attributes
     * @return friendship - corresponding friendship
     */
    @Override
    protected Friendship extractEntity(List<String> attributes) {
        Friendship friendship = new Friendship();
        friendship.setID(new Tuple<>(Long.parseLong(attributes.get(0)), Long.parseLong(attributes.get(1))));

        return friendship;
    }

    /**
     * Saves a friendship to the file.
     * @param entity - entity to be saved, must not be null
     * @return the value of superclass save
     */
    @Override
    public Friendship save(Friendship entity) {
        //check if entity doesnt exists as A B or B A
        Friendship mirroredFriendship = new Friendship();
        mirroredFriendship.setID(new Tuple<>(entity.getID().getRight(), entity.getID().getLeft()));

        if(super.findOne(entity.getID()) != null || super.findOne(mirroredFriendship.getID()) != null){
            //friendship already exists
            return entity;
        }

        return super.save(entity);
    }

    /**
     * Removes friendship from file
     * @param friendshipID - of friendship
     * @return superclass value of remove
     */
    @Override
    public Friendship remove(Tuple<Long, Long> friendshipID) {
        Friendship removed = super.remove(friendshipID);

        if(removed != null){
            //friendship was found and removed
            return removed;
        }

        //try removing the mirrored friendship relationship from repo and return the result
        return super.remove(new Tuple<>(friendshipID.getRight(), friendshipID.getLeft()));
    }

    @Override
    public Friendship update(Friendship entity) {
        return null;
    }
}


// ex.txt