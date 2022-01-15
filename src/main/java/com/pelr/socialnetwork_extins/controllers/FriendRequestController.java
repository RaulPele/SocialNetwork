package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.MainApplication;
import com.pelr.socialnetwork_extins.domain.DTOs.FriendRequestDTO;
import com.pelr.socialnetwork_extins.domain.User;
import com.pelr.socialnetwork_extins.service.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class FriendRequestController {
    private FriendRequestDTO requestDTO;
    private Controller controller;

    @FXML
    private Button acceptButton;

    @FXML
    private Button declineButton;

    @FXML
    private ImageView profileImageView;

    @FXML
    private Label nameLabel;

    @FXML
    private VBox rootLayout;

    public void initializeRequest(FriendRequestDTO requestDTO) {
        this.requestDTO = requestDTO;

        nameLabel.setText(requestDTO.getFirstName() + " " + requestDTO.getLastName());
        loadRequestPicture();
    }

    private void loadRequestPicture() {
        String pictureName = requestDTO.getEmail()+".jpeg";

        String pictureURL = String.valueOf(MainApplication.class.getResource("assets/" + pictureName));
        if (pictureURL.equals("null")) {
            pictureURL = String.valueOf(MainApplication.class.getResource("assets/unknown_user.png"));
        }
        profileImageView.setImage(new Image(pictureURL));
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

}
