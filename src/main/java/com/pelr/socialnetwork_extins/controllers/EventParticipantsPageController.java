package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.MainApplication;
import com.pelr.socialnetwork_extins.SceneManager;
import com.pelr.socialnetwork_extins.domain.Event;
import com.pelr.socialnetwork_extins.domain.User;
import com.pelr.socialnetwork_extins.service.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class EventParticipantsPageController {
    private Controller controller;
    private Event event;

    @FXML
    private GridPane participantsGridPane;

    private static final int MAX_COLUMN_COUNT = 2;
    private int rowCount, columnCount;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void initializeParticipantsPage(Event event){
        this.event = event;
        loadParticipants();
    }

    private void loadParticipants() {
        event.getParticipants().forEach(participant -> {
            Node participantCardView = createParticipantCardView(participant);
            addItemToLayout(participantCardView);
        });
    }

    private Node createParticipantCardView(User participant) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/friend_item_big-view.fxml"));

        try {
            Parent participantView =fxmlLoader.load();
            Label friendNameLabel = (Label) participantView.lookup("#friendItemNameLabel");
            friendNameLabel.setText(participant.getFirstName() + " " + participant.getLastName());

            ImageView friendProfilePicture = (ImageView) participantView.lookup("#friendItemImageView");
            friendProfilePicture.setImage(new Image(String.valueOf(MainApplication.class.getResource("assets/unknown_user.png"))));

            return participantView;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void addItemToLayout(Node item) {
        participantsGridPane.add(item, columnCount, rowCount);
        columnCount++;
        if(columnCount == MAX_COLUMN_COUNT) {
            columnCount = 0;
            rowCount++;
        }
    }

}
