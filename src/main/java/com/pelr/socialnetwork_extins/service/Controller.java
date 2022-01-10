package com.pelr.socialnetwork_extins.service;


import com.pelr.socialnetwork_extins.domain.DTOs.FriendRequestDTO;
import com.pelr.socialnetwork_extins.domain.DTOs.FriendshipDTO;

import com.pelr.socialnetwork_extins.domain.DTOs.ConversationHeaderDTO;

import com.pelr.socialnetwork_extins.domain.Friendship;
import com.pelr.socialnetwork_extins.domain.Graph;
import com.pelr.socialnetwork_extins.domain.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Controller class that manages application services.
 */
public class Controller {
    private UserService userService;
    private FriendshipService friendshipService;
    private Authentication authentication;
    private MessagingService messagingService;

    /**
     * Creates a controller containing multiple services.
     * Loads friend list for all users on creation.
     *
     * @param userService       - user service used by the controller
     * @param friendshipService - friendship service used by the controller
     * @param authentication    - authentication service used by the controller
     */
    public Controller(UserService userService, FriendshipService friendshipService, Authentication authentication, MessagingService messagingService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.authentication = authentication;
        this.messagingService = messagingService;
    }

    /**
     * Saves a new user in application.
     *
     * @param firstName - first name string of the new user
     * @param lastName  - last name string of the new user
     * @param email     - email of the new user
     */
    public void addUser(String firstName, String lastName, String email, String password) {
        userService.save(firstName, lastName, email, password);
    }

    /**
     * Removes a user from the application.
     *
     * @param email - email of the user that is being removed
     */
    public void removeUser(String email){
        if(authentication.isLoggedIn() && email.equals(authentication.getLoggedUser().getEmail())){
            authentication.logout();
        }
        friendshipService.removeAllContaining(userService.findIDByUserEmail(email));
        userService.remove(email);
    }

    /**
     * Returns a collection of all the users that exist.
     *
     * @return users - Iterable containing User objects
     */
    public Iterable<User> findAllUsers(){
        return userService.findAll();
    }

    /**
     * Logs in specified user
     *
     * @param email - email of user that tries to log in
     */
    public void login(String email, String password){
        authentication.login(email, password);
    }

    /**
     * Logs out current user.
     */
    public void logout(){
        authentication.logout();
    }

    /**
     * Returns a string of information about the current user
     *
     * @return userInfo - String of user information; userInfo is empty if no user is logged in.
     */
    public String getLoggedUserInfo(){
        StringBuilder userInfo = new StringBuilder();

        try {
            User user = authentication.getLoggedUser();
            userInfo.append(user.getFirstName()).append(" ").append(user.getLastName()).append(", Email: ").append(user.getEmail());

        } catch(AuthenticationException ignored) {}

        return userInfo.toString();
    }

    public String getNameOfLoggedUser()
    {
        StringBuilder userName = new StringBuilder();
        try {
            User user = authentication.getLoggedUser();
            userName.append("User:  ").append(user.getFirstName()).append(" ").append(user.getLastName());
        }
        catch (AuthenticationException ignored) {

        }

        return userName.toString();
    }

    /**
     * Save friendship between current user and specified user.
     *
     * @param friendEmail - email of the specified user
     */
    public void saveFriendship(String friendEmail){
        User loggedUser = authentication.getLoggedUser();
        friendshipService.save(loggedUser.getID(), userService.findIDByUserEmail(friendEmail));
    }

    /**
     * Removes a friendship between current user and specified user.
     *
     * @param friendEmail - email of the specified user
     */
    public void removeFriendship(String friendEmail) {
        User loggedUser = authentication.getLoggedUser();
        friendshipService.remove(loggedUser.getID(), userService.findIDByUserEmail(friendEmail));
    }

    /**
     * Returns a collection containing all the friends of the current user.
     *
     * @return friendshipDTOs - List containing the friends of current user
     */
    public Iterable<FriendshipDTO> getFriendsOfLoggedUser() {
        User loggedUser = authentication.getLoggedUser();
        Iterable<Friendship> friendships = friendshipService.getFriendships(loggedUser.getID());

        List<FriendshipDTO> friendshipDTOs =
        StreamSupport.stream(friendships.spliterator(),false)
                .map(friendship -> {
                    Long friendID;
                    if (!friendship.getID().getLeft().equals(loggedUser.getID())) {
                        friendID = friendship.getID().getLeft();
                    }
                    else {
                        friendID = friendship.getID().getRight();
                    }

                    String firstName, lastName,date, email;

                    User friend = userService.findOne(friendID);
                    firstName = friend.getFirstName();
                    lastName = friend.getLastName();
                    date = friendship.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    email = friend.getEmail();
                    return new FriendshipDTO(firstName,lastName,date, email);
                })
                .collect(Collectors.toList());

        return friendshipDTOs;
    }

    /**
     * Returns the number of communities in the network.
     *
     * @return numberOfCommunities - number of communities
     */
    public Long getNumberOfCommunities(){
        return (long) generateFriendshipsGraph().getConnectedComponents().size();
    }

    /**
     * Returns a collection of all the members of the most sociable community.
     *
     * @return members - Iterable contains all the members.
     */
    public Iterable<User> getMostSociableCommunityMembers(){
        Iterable<Long> memberIDs = findMostSociableCommunityMembers();
        List<User> members = new ArrayList<>();

        memberIDs.forEach((memberID) -> {
            members.add(userService.findOne(memberID));
        });

        return members;
    }

    /**
     * Returns a collection of all the members that are part of the longest path of friends
     * from the most sociable community.
     *
     * @return members - Iterable users
     */
    public Iterable<User> getMostSociableCommunityChain(){
        Iterable<Long> memberIDs = findLongestFriendChain();
        List<User> members = new ArrayList<>();

        memberIDs.forEach((memberID) ->{
                members.add(userService.findOne(memberID));
        });

        return members;
    }

    /**
     * Generate a graph containing all the friend relationships as edges and users as vertices.
     *
     * @return graph - Graph object
     */
    public Graph generateFriendshipsGraph(){
        Iterable<Friendship> friendships = friendshipService.findAll();
        Iterable<User> users = userService.findAll();
        Graph graph = new Graph();

        users.forEach(user -> {
            graph.addNode(user.getID());
        });

        friendships.forEach(friendship -> {
            graph.addEdge(friendship.getID());
        });

        return graph;
    }

    /**
     * Returns the members of the most sociable communities.
     *
     * @return members - Iterable containing member ID's.
     */
    public Iterable<Long> findMostSociableCommunityMembers(){
        return findMostSociableCommunity().getNodes();
    }

    /**
     * Returns the most sociable community.
     *
     * @return mostSociableCommunity - Graph object representing the most sociable community
     */
    public Graph findMostSociableCommunity(){
        Graph graph = generateFriendshipsGraph();
        Graph mostSociableCommunity = graph.longestPathComponent().getLeft();

        return mostSociableCommunity;
    }

    /**
     * Returns the longest path of friends in the most sociable community.
     *
     * @return path - Iterable containing the users.
     */
    public Iterable<Long> findLongestFriendChain(){
        return generateFriendshipsGraph().longestPathComponent().getRight();
    }


    public Iterable<ConversationHeaderDTO> getConversationHeaders(){
        List<ConversationHeaderDTO> conversationHeaders = new ArrayList<>();

        userService.findAll().forEach(user -> {
            ConversationHeaderDTO conversationHeader = new ConversationHeaderDTO(user.getFirstName(), user.getLastName(), user.getEmail());
            conversationHeaders.add(conversationHeader);
        });

        return conversationHeaders;
    }

    public boolean userIsLoggedIn(){
        try {
            authentication.getLoggedUser();
            return true;
        } catch (AuthenticationException ex) {
            return false;
        }
    }

    public ChatRoom createChatRoom(String email) {
        return new ChatRoom(messagingService, userService, authentication.getLoggedUser(), userService.findUserByEmail(email));
    }

    public void sendMessageToMultipleUsers(String message, String userEmails){
        List<User> receivers = userService.getUserListFromEmailsString(userEmails);
        messagingService.save(authentication.getLoggedUser(), receivers, message, null);
    }

    public Iterable<FriendRequestDTO> getReceivedFriendRequests() {
        User loggedUser = authentication.getLoggedUser();
        List<FriendRequestDTO> friendRequestDTOs = new ArrayList<>();
        Iterable<Friendship> receivedRequests = friendshipService.getReceivedFriendRequests(loggedUser.getID());

        receivedRequests.forEach(request -> {
            String lastName, firstName, email, status;
            User sender = userService.findOne(request.getID().getLeft());
            lastName = sender.getLastName();
            firstName = sender.getFirstName();
            email = sender.getEmail();
            status = request.getStatus().toString();
            LocalDateTime date = request.getDate();
            FriendRequestDTO friendRequestDTO = new FriendRequestDTO(firstName, lastName, email, status, date);

            friendRequestDTOs.add(friendRequestDTO);
        });

        return friendRequestDTOs;
    }

    public Iterable<FriendRequestDTO> getAllFriendRequests() {
        User loggedUser = authentication.getLoggedUser();
        List<FriendRequestDTO> friendRequestDTOs = new ArrayList<>();
        Iterable<Friendship> receivedRequests = friendshipService.getAllFriendRequests(loggedUser.getID());

        receivedRequests.forEach(request -> {
            String lastName, firstName, email, status;
            User sender = userService.findOne(request.getID().getLeft());
            lastName = sender.getLastName();
            firstName = sender.getFirstName();
            email = sender.getEmail();
            status = request.getStatus().toString();
            LocalDateTime date = request.getDate();
            FriendRequestDTO friendRequestDTO = new FriendRequestDTO(firstName, lastName, email, status, date);

            friendRequestDTOs.add(friendRequestDTO);
        });

        return friendRequestDTOs;
    }

    public void acceptFriendRequest(String friendEmail) {
        Long friendID = userService.findIDByUserEmail(friendEmail);
        Long loggedUserID = authentication.getLoggedUser().getID();

        friendshipService.declineFriendRequest(friendID, loggedUserID);
    }

    public void declineFriendRequest(String friendEmail) {
        Long friendID = userService.findIDByUserEmail(friendEmail);
        Long loggedUserID = authentication.getLoggedUser().getID();

        friendshipService.acceptFriendRequest(friendID, loggedUserID);
    }

    public Iterable<FriendshipDTO> getFriendshipsFromDate(int year, String month) {
        User loggedUser = authentication.getLoggedUser();
        List<FriendshipDTO> friendshipDTOs = new ArrayList<>();
        Iterable<Friendship> friendships = friendshipService.getUserFriendshipsFromDate(loggedUser.getID(), year, month);

        friendshipDTOs = StreamSupport.stream(friendships.spliterator(), false)
                .map(friendship -> {
                    Long friendID;

                    if(!friendship.getID().getLeft().equals( loggedUser.getID())){
                        friendID = friendship.getID().getLeft();
                    }else{
                        friendID = friendship.getID().getRight();
                    }

                    User friend = userService.findOne(friendID);

                    return new FriendshipDTO(friend.getFirstName(), friend.getLastName(), friendship.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), friend.getEmail());
                })
                .collect(Collectors.toList());

        return friendshipDTOs;
    }

    public User getLoggedUser(){
        return authentication.getLoggedUser();
    }
}




