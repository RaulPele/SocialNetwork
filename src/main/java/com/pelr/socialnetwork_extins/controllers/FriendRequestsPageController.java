package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.MainApplication;
import com.pelr.socialnetwork_extins.SceneManager;
import com.pelr.socialnetwork_extins.domain.DTOs.FriendRequestDTO;
import com.pelr.socialnetwork_extins.service.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class FriendRequestsPageController {
    private SceneManager sceneManager;
    private Controller controller;

    @FXML
    private Button homeButton;

    @FXML
    private AnchorPane rootLayout;

    @FXML
    private ScrollPane requestsScrollPane;

    @FXML
    private FlowPane requestsPane;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @FXML
    public void initialize() {
        requestsScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        requestsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    public void initializeScreen() {
        loadFriendRequests();
    };

    private Node createFriendRequestCard(FriendRequestDTO requestDTO){
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/friend_request.fxml"));
        FriendRequestController friendRequestController = new FriendRequestController();
        fxmlLoader.setController(friendRequestController);

        try {
            Parent requestView = fxmlLoader.load();
            Label nameLabel = (Label) requestView.lookup("#nameLabel");
            nameLabel.setText(requestDTO.getFirstName() + " " + requestDTO.getLastName());

            Button acceptButton = (Button) requestView.lookup("#acceptButton");
            Button declineButton = (Button) requestView.lookup("#declineButton");

            acceptButton.setOnAction(event -> onAcceptButtonClicked(event, requestDTO.getEmail()));
            declineButton.setOnAction(event -> onDeclineButtonClicked(event, requestDTO.getEmail()));

            return requestView;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void loadFriendRequests() {
        Iterable<FriendRequestDTO> friendRequests = controller.getReceivedFriendRequests();
        friendRequests.forEach(requestDTO -> {
            Node requestCard = createFriendRequestCard(requestDTO);
            requestsPane.getChildren().add(requestCard);
        });
    }

    public void onHomeButtonClicked(ActionEvent actionEvent) {
        changeToHomePageScreen();
    }

    private void changeToHomePageScreen() {
        try {
            sceneManager.changeToHomePageScene();
            sceneManager.centerStageOnScreen();
            HomePageController homePageController = sceneManager.getHomePageController();
            homePageController.setController(controller);
            homePageController.setSceneManager(sceneManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onAcceptButtonClicked(ActionEvent event, String email) {
        controller.acceptFriendRequest(email);
        refreshScreen();
    }

    private void onDeclineButtonClicked(ActionEvent event, String email) {
        controller.declineFriendRequest(email);
        refreshScreen();
    }

    private void refreshScreen(){
        requestsPane.getChildren().clear();
        loadFriendRequests();
    }
}
