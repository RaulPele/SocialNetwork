package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.MainApplication;
import com.pelr.socialnetwork_extins.SceneManager;
import com.pelr.socialnetwork_extins.domain.Page;
import com.pelr.socialnetwork_extins.domain.User;
import com.pelr.socialnetwork_extins.service.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.List;

public class ProfilePageController {
    private Controller controller;
    private SceneManager sceneManager;
    private Page profilePage;

    @FXML
    private AnchorPane rootLayout;

    @FXML
    private Label profileNameLabel;

    @FXML
    private ImageView profilePictureImageView;

    @FXML
    private Button requestButton;

    @FXML
    private Button homeButton;

    @FXML
    private GridPane friendsGridPane;

    private int rowCount, columnCount;
    private static final int MAX_ROW_COUNT=2;
    private static final int MAX_COLUMN_COUNT = 3;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void initializeProfile(String email) {
        profilePage = controller.createProfilePage(email);
        User profileOwner = profilePage.getProfileOwner();

        profileNameLabel.setText(profileOwner.getFirstName() + " " + profileOwner.getLastName());

        if(profileOwner.getEmail().equals(controller.getLoggedUser().getEmail())) {
            rootLayout.getChildren().remove(requestButton);
           // requestButton.setVisible(false);
        }

        profilePictureImageView.setImage(new Image(String.valueOf(MainApplication.class.getResource("assets/unknown_user.png"))));

        loadFriendList();
    }

    private void loadFriendList() {
        List<User> friendList = profilePage.getFriendList();
        friendList.forEach(friend -> {
            if(rowCount >= MAX_ROW_COUNT) {
                return;
            }

            Node friendItemView = createFriendItemView(friend);
            addFriendItemToLayout(friendItemView);
        });
    }

    private Node createFriendItemView(User user){
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/friend_item-view.fxml"));

        try {
            Parent friendItemView =fxmlLoader.load();
            Label friendNameLabel = (Label) friendItemView.lookup("#friendItemNameLabel");
            friendNameLabel.setText(user.getFirstName() + " " + user.getLastName());

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
            if(rowCount < MAX_ROW_COUNT) {
                columnCount = 0;
                rowCount++;
            }
        }
    }

    public void onHomeButtonClicked(ActionEvent actionEvent) {
        changeToHomeScreen();
    }

    private void changeToHomeScreen() {
        try {
            sceneManager.changeToHomePageScene();
            sceneManager.centerStageOnScreen();
            HomePageController homePageController = sceneManager.getHomePageController();
            homePageController.setController(controller);
            homePageController.setSceneManager(sceneManager);
            homePageController.initializeScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSeeAllFriendsLabelClicked(MouseEvent mouseEvent) {
        changeToFriendsScreen();
    }

    private void changeToFriendsScreen() {
        try{
            sceneManager.changeToFriendsPageScene();
            sceneManager.centerStageOnScreen();
            FriendsPageController friendsPageController = sceneManager.getFriendsPageController();
            friendsPageController.setController(controller);
            friendsPageController.setSceneManager(sceneManager);
            friendsPageController.initializeScreen(profilePage.getProfileOwner().getEmail(), profilePage.getFriendList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
