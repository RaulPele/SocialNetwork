package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.MainApplication;
import com.pelr.socialnetwork_extins.SceneManager;
import com.pelr.socialnetwork_extins.domain.Page;
import com.pelr.socialnetwork_extins.domain.User;
import com.pelr.socialnetwork_extins.service.Controller;
import com.pelr.socialnetwork_extins.utils.Observer;
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

import java.io.IOException;
import java.util.List;

public class ProfilePageController implements Observer {
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
        controller.addObserver(this);
        profilePage = controller.createProfilePage(email);
        User profileOwner = profilePage.getProfileOwner();

        profileNameLabel.setText(profileOwner.getFirstName() + " " + profileOwner.getLastName());

        loadRequestButton();

        profilePictureImageView.setImage(new Image(String.valueOf(MainApplication.class.getResource("assets/unknown_user.png"))));

        loadFriendList();
        loadProfilePicture();
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
            FriendItemController friendItemController = fxmlLoader.getController();
            friendItemController.initializeFriendItem(user);

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

    private void loadRequestButton() {
        User profileOwner = profilePage.getProfileOwner();

        if(profileOwner.getEmail().equals(controller.getLoggedUser().getEmail())) {
            rootLayout.getChildren().remove(requestButton);
            // requestButton.setVisible(false);
        }else if(controller.isFriend(profileOwner.getEmail())) {
            requestButton.setText("Unfriend");
            requestButton.setOnAction(this::onUnfriendButtonClicked);
        } else if(controller.requestIsRejected(profileOwner.getEmail())){
            requestButton.setText("Your request was rejected.");
            requestButton.setDisable(true);
        } else if(controller.hasIncomingFriendRequest(profileOwner.getEmail())){
            requestButton.setText("Accept friend request");
            requestButton.setOnAction(this::onAcceptRequestButtonClicked);
        } else if(controller.hasOutgoingFriendRequest(profileOwner.getEmail())) {
            requestButton.setText("Unsend friend request");
            requestButton.setOnAction(this::onUnsendButtonClicked);
        } else{
            requestButton.setText("Add friend");
            requestButton.setOnAction(this::onAddFriendButtonClicked);
        }
    }

    private void onUnfriendButtonClicked(ActionEvent actionEvent) {
        controller.removeFriendship(profilePage.getProfileOwner().getEmail());
        requestButton.setText("Add friend");
        requestButton.setOnAction(this::onAddFriendButtonClicked);
    }

    private void onAcceptRequestButtonClicked(ActionEvent actionEvent) {
        controller.acceptFriendRequest(profilePage.getProfileOwner().getEmail());
        requestButton.setText("Unfriend");
        requestButton.setOnAction(this::onUnfriendButtonClicked);
    }

    private void onUnsendButtonClicked(ActionEvent actionEvent) {
        controller.unsendFriendRequest(profilePage.getProfileOwner().getEmail());
        requestButton.setText("Add friend");
        requestButton.setOnAction(this::onAddFriendButtonClicked);
    }

    private void onAddFriendButtonClicked(ActionEvent actionEvent) {
        controller.sendFriendRequest(profilePage.getProfileOwner().getEmail());
        requestButton.setText("Unsend friend request");
        requestButton.setOnAction(this::onUnsendButtonClicked);
    }

    private void reloadFriendList() {
        profilePage.setFriendList(controller.getFriends(profilePage.getProfileOwner().getEmail()));
        friendsGridPane.getChildren().clear();
        columnCount = rowCount=0;
        loadFriendList();
    }

    @Override
    public void update() {
        reloadFriendList();
    }

    private void loadProfilePicture() {
        String pictureName = profilePage.getProfileOwner().getEmail()+".jpeg";

        String pictureURL = String.valueOf(MainApplication.class.getResource("assets/" + pictureName));
        if (pictureURL.equals("null")) {
            pictureURL = String.valueOf(MainApplication.class.getResource("assets/unknown_user.png"));
        }
        profilePictureImageView.setImage(new Image(pictureURL));
    }
}
