package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.MainApplication;
import com.pelr.socialnetwork_extins.domain.Event;
import com.pelr.socialnetwork_extins.service.Controller;
import com.pelr.socialnetwork_extins.utils.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EventCardController implements Observer{
    private Controller controller;
    private Event event;

    @FXML
    private Button attendButton;

    @FXML
    private ImageView eventImageView;

    @FXML
    private Label dateLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label participantsNumberLabel;

    @FXML
    private Label locationLabel;


    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void initializeEvent(Event event) {
        this.event = event;

        Image eventImage = new Image(String.valueOf(MainApplication.class.getResource("assets/unknown_user.png")));
        // eventImageView.setImage(eventImage);
        dateLabel.setText(event.getDate().getDayOfWeek() + ", " + event.getDate().getMonth() + " " + event.getDate().getDayOfMonth());
        titleLabel.setText(event.getTitle());
        locationLabel.setText(event.getLocation());
        participantsNumberLabel.setText(event.getParticipants().size() + " people attending");

        if(event.isAttending(controller.getLoggedUser())) {
            attendButton.setText("Attending");
        }else {
            attendButton.setText("Attend");
        }

        controller.addObserver(this);
    }

    public void onAttendButtonClicked(ActionEvent actionEvent) {
        if(event.isAttending(controller.getLoggedUser())) {
            cancelAttendingToEvent();
        }else {
            attendToEvent();
        }
    }

    private void cancelAttendingToEvent() {
        attendButton.setText("Attend");
        controller.cancelAttendingToEvent(event);

    }

    private void attendToEvent() {
        attendButton.setText("Attending");
        controller.attendToEvent(event);
    }

    @Override
    public void update() {
        participantsNumberLabel.setText(event.getParticipants().size() + " people attending");
    }
}
