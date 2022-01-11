package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.MainApplication;
import com.pelr.socialnetwork_extins.SceneManager;
import com.pelr.socialnetwork_extins.domain.User;
import com.pelr.socialnetwork_extins.service.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class FriendsPageController {
    private Controller controller;
    private SceneManager sceneManager;
    private List<User> friendList;
    private String userEmail;

    @FXML
    private GridPane friendsGridPane;

    @FXML
    private Button backButton;

    private static final int MAX_COLUMN_COUNT = 2;

    private int rowCount, columnCount;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    private void setFriendList(List<User> friendList) {
        this.friendList = friendList;
    }


    public void initializeScreen(String email, List<User> friendList) {
        setFriendList(friendList);
        userEmail = email;
        loadFriends();
    }

    private void loadFriends() {
        friendList.forEach(friend -> {
            Node friendItemView = createFriendItemView(friend);
            addFriendItemToLayout(friendItemView);
        });
    }

    private Node createFriendItemView(User friend){
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/friend_item_big-view.fxml"));

        try {
            Parent friendItemView =fxmlLoader.load();
            Label friendNameLabel = (Label) friendItemView.lookup("#friendItemNameLabel");
            friendNameLabel.setText(friend.getFirstName() + " " + friend.getLastName());

            ImageView friendProfilePicture = (ImageView) friendItemView.lookup("#friendItemImageView");
            friendProfilePicture.setImage(new Image(String.valueOf(MainApplication.class.getResource("assets/unknown_user.png"))));

            return friendItemView;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addFriendItemToLayout(Node friendItemView) {
        friendsGridPane.add(friendItemView, columnCount, rowCount);
        columnCount++;
        if(columnCount == MAX_COLUMN_COUNT) {
            columnCount = 0;
            rowCount++;
        }
    }

    public void onBackButtonClicked(ActionEvent actionEvent) {
        changeToProfilePageScreen();
    }

    private void changeToProfilePageScreen(){
        try {
            sceneManager.changeToProfilePageScene();
            sceneManager.centerStageOnScreen();
            ProfilePageController profilePageController = sceneManager.getProfilePageController();
            profilePageController.setController(controller);
            profilePageController.setSceneManager(sceneManager);
            profilePageController.initializeProfile(userEmail);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
