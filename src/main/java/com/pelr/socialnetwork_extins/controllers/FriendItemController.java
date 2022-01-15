package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.MainApplication;
import com.pelr.socialnetwork_extins.domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FriendItemController {
    private User friend;

    @FXML
    private Label friendItemNameLabel;

    @FXML
    private ImageView friendItemImageView;

    public void initializeFriendItem(User friend) {
        this.friend = friend;
        loadFriend();
    }

    private void loadFriend() {
        friendItemNameLabel.setText(friend.getFirstName() + " " + friend.getLastName());
        loadFriendPicture();
    }

    private void loadFriendPicture() {
        String pictureName = friend.getEmail() +".jpeg";

        String pictureURL = String.valueOf(MainApplication.class.getResource("assets/" + pictureName));
        if (pictureURL.equals("null")) {
            pictureURL = String.valueOf(MainApplication.class.getResource("assets/unknown_user.png"));
        }
        friendItemImageView.setImage(new Image(pictureURL));
    }
}
