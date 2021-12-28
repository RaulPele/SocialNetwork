package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.MainApplication;
import com.pelr.socialnetwork_extins.domain.User;
import com.pelr.socialnetwork_extins.service.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class FriendRequestController {

    private Controller controller;
    private User sender;

    @FXML
    private Button acceptButton;

    @FXML
    private Button declineButton;

    @FXML
    private ImageView profileImageView;

    @FXML
    private VBox rootLayout;

    public void setSender(User requestSender){
        this.sender = requestSender;
    }

    @FXML
    public void initialize() {
        Image image = new Image(String.valueOf(MainApplication.class.getResource("assets/unknown_user.png")));
        profileImageView.setImage(image);

    }

}
