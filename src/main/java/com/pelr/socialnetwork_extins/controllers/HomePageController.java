package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.MainApplication;
import com.pelr.socialnetwork_extins.SceneManager;
import com.pelr.socialnetwork_extins.service.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class HomePageController {

    private SceneManager sceneManager;
    private Controller controller;

    @FXML
    private ImageView profileImageView;

    @FXML
    private AnchorPane rootLayout;

    @FXML
    private Button friendRequestsButton;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() throws IOException {

    }


    public void onFriendRequestsButtonClicked(ActionEvent actionEvent) {
        changeToFriendRequestsScreen();
    }

    private void changeToFriendRequestsScreen(){
        try {
            sceneManager.changeToFriendRequestsPageScene();
            sceneManager.centerStageOnScreen();
            FriendRequestsPageController friendRequestsPageController = sceneManager.getFriendRequestsPageController();
            friendRequestsPageController.setSceneManager(sceneManager);
            friendRequestsPageController.setController(controller);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
