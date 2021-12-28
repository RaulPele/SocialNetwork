package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.MainApplication;
import com.pelr.socialnetwork_extins.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class FriendRequestsPageController {

    private SceneManager sceneManager;

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

    @FXML
    public void initialize() throws IOException {


        for (int i = 0; i < 10; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/friend_request.fxml"));
            FriendRequestController friendRequestController = new FriendRequestController();
            fxmlLoader.setController(friendRequestController);

            Node requestView = fxmlLoader.load();
            requestsPane.getChildren().add(requestView);

        }
        requestsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    }

}
