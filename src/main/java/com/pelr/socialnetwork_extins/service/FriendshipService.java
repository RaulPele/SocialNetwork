package com.pelr.socialnetwork_extins.service;

import com.pelr.socialnetwork_extins.domain.Friendship;
import com.pelr.socialnetwork_extins.domain.Graph;
import com.pelr.socialnetwork_extins.domain.Status;
import com.pelr.socialnetwork_extins.domain.Tuple;
import com.pelr.socialnetwork_extins.repository.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * FriendshipService class for the friendship service.
 */
public class FriendshipService {
    private Repository<Tuple<Long, Long>, Friendship> friendshipRepository;

    /**
     * Creates a friendship service that uses the specified friendship repository.
     * @param friendshipRepository - Repository of friendships
     */
    public FriendshipService(Repository<Tuple<Long, Long>, Friendship> friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    /**
     * Saves a friendship between two users.
     * @param userID1 - ID of a user
     * @param userID2 - ID of another user
     * @return savedFriendship - friendship entity if the friendship
     * already exists in the repository
     *
     * @throws FriendshipException if the friendship already exists
     */
    public Friendship save(Long userID1, Long userID2){
        if(userID1.equals(userID2)){
            throw new FriendshipException("The provided ID's must be different!\n");
        }

        Friendship friendship;
        Tuple<Long,Long> id = new Tuple<>(userID1, userID2) ;
        Tuple<Long, Long> mirroredId = new Tuple<>(userID2, userID1);
        Friendship mirroredFriendship = friendshipRepository.findOne(mirroredId);

        friendship = friendshipRepository.findOne(id);
        Friendship savedFriendship = null;
        if (mirroredFriendship != null)
        {
            switch ( mirroredFriendship.getStatus())
            {
                case APPROVED : throw new FriendshipException("Friendship already exists!");
                case REJECTED :
                    friendshipRepository.remove(mirroredId);
                    friendship = new Friendship();
                    friendship.setID(id);
                    friendship.setStatus(Status.PENDING);
                    friendshipRepository.save(friendship);
                    break;

                case PENDING : throw new FriendshipException("You have a request from this user already in Request Friendship.");
            }
        }
        else if(friendship != null)
        {
            switch ( friendship.getStatus())
            {
                case APPROVED -> throw new FriendshipException("Friendship already exists!");
                case REJECTED -> throw new FriendshipException("You have been already rejected!");
                case PENDING -> throw new FriendshipException("You have already send a request to this user.");
            }
        }
        else
        {
            friendship = new Friendship();
            friendship.setID(id);
            friendship.setStatus(Status.PENDING);
            savedFriendship = friendshipRepository.save(friendship);

        }
        return savedFriendship;
    }


    /**
     * Removes a friendship between two users.
     * @param userID1 - ID of a user
     * @param userID2 - ID of other user
     * @return removedFriendship - entity of friendship that is removed
     *
     * @throws FriendshipException if the friendship was not found
     */
    public Friendship remove(Long userID1, Long userID2){
        if(userID1.equals(userID2)){
            throw new FriendshipException("The provided ID's must be different!\n");
        }

        Friendship removedFriendship = friendshipRepository.remove(new Tuple<>(userID1, userID2));

        if(removedFriendship == null){
            throw new FriendshipException("Friendship not found!");
        }

        return removedFriendship;
    }

    /**
     * Returns a collection of all users.
     * @return users - Iterable
     */
    public Iterable<Friendship> findAll(){
        return friendshipRepository.findAll();
    }

    public void removeAllContaining(Long userID){
        Iterable<Friendship> friendships = friendshipRepository.findAll();

        friendships.forEach((friendship) -> {
            if(friendship.getID().getLeft().equals(userID) || friendship.getID().getRight().equals(userID)){
                friendshipRepository.remove(friendship.getID());
            }
        });
    }

    public Iterable<Long> getFriends(Long userID){
        List<Long> friendsIDs = new ArrayList<>();
        Iterable<Friendship> friendships = friendshipRepository.findAll();

        friendships.forEach(friendship ->{
            Tuple<Long, Long> friendshipID = friendship.getID();

            if(friendshipID.getLeft().equals(userID)){
                friendsIDs.add(friendshipID.getRight());
            } else if(friendshipID.getRight().equals(userID)) {
                friendsIDs.add(friendshipID.getLeft());
            }
        });

        return friendsIDs;
    }

    /**
     *
     * @param userID - ID of the current user
     * @return all the current user's friendships
     */
    public Iterable<Friendship> getFriendships(Long userID) {
        List<Friendship> userFriendships ;
        Iterable<Friendship> friendships = friendshipRepository.findAll();

        userFriendships = StreamSupport.stream(friendships.spliterator(),false)
                .filter(friendship-> friendship.getID().getLeft().equals(userID) ||
                        friendship.getID().getRight().equals(userID))
                .filter(friendship -> friendship.getStatus() == Status.APPROVED)
                .collect(Collectors.toList());

        return userFriendships;
    }

    public Iterable<Friendship> getReceivedFriendRequests(Long userID) {
        List<Friendship> receivedRequest;
        Iterable<Friendship> friendships = friendshipRepository.findAll();

        receivedRequest = StreamSupport.stream(friendships.spliterator(),false)
                .filter(friendship->friendship.getID().getRight().equals(userID))
                .filter(friendship -> friendship.getStatus() == Status.PENDING)
                .collect(Collectors.toList());

        return receivedRequest;
    }

    public Iterable<Friendship> getAllFriendRequests(Long userID){
        List<Friendship> receivedRequest;
        Iterable<Friendship> friendships = friendshipRepository.findAll();

        receivedRequest = StreamSupport.stream(friendships.spliterator(),false)
                .filter(friendship->friendship.getID().getRight().equals(userID))
                .collect(Collectors.toList());

        return receivedRequest;
    }

    public void declineFriendRequest(Long friendID, Long loggedUserID) {
        Tuple<Long, Long> idFriendship = new Tuple<>(friendID, loggedUserID);
        Friendship newStatusFriendship = new Friendship();

        newStatusFriendship.setStatus(Status.REJECTED);
        newStatusFriendship.setID(idFriendship);

        friendshipRepository.update(newStatusFriendship);
    }

    public void acceptFriendRequest(Long friendID, Long loggedUserID) {
        Tuple<Long, Long> idFriendship = new Tuple<>(friendID, loggedUserID);
        Friendship newStatusFriendship = new Friendship();

        newStatusFriendship.setStatus(Status.APPROVED);
        newStatusFriendship.setDate(LocalDateTime.now());
        newStatusFriendship.setID(idFriendship);

        friendshipRepository.update(newStatusFriendship);
    }

    public Iterable<Friendship> getUserFriendshipsFromDate(Long userID, int year, String month){
        Iterable<Friendship> friendships = friendshipRepository.findAll();
        List<Friendship> friendshipsFromDate;

        friendshipsFromDate = StreamSupport.stream(friendships.spliterator(), false)
                .filter(friendship -> friendship.getID().getLeft().equals(userID) || friendship.getID().getRight().equals(userID))
                .filter(friendship -> friendship.getDate().getMonth().toString().equals(month) && friendship.getDate().getYear() == year)
                .collect(Collectors.toList());

        return friendshipsFromDate;
    }
}