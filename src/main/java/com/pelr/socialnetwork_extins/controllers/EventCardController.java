package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.MainApplication;
import com.pelr.socialnetwork_extins.SceneManager;
import com.pelr.socialnetwork_extins.domain.Event;
import com.pelr.socialnetwork_extins.service.Controller;
import com.pelr.socialnetwork_extins.utils.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class EventCardController implements Observer{
    private Controller controller;
    private Event event;
    private SceneManager sceneManager;

    @FXML
    private VBox eventCardLayout;

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

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void initializeEvent(Event event) {
        this.event = event;

        loadEventImage();
        dateLabel.setText(event.getDate().getDayOfWeek() + ", " + event.getDate().getMonth() + " " + event.getDate().getDayOfMonth());
        titleLabel.setText(event.getTitle());
        locationLabel.setText(event.getLocation());
        participantsNumberLabel.setText(event.getParticipants().size() + " people attending");

        if(event.isAttending(controller.getLoggedUser())) {
            attendButton.setText("Attending");
        }else {
            attendButton.setText("Attend");
        }

        eventCardLayout.setOnMouseClicked(this::onEventCardClicked);
        //controller.addObserver(this);
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
        participantsNumberLabel.setText(event.getParticipants().size() + " people attending");

    }

    private void attendToEvent() {
        attendButton.setText("Attending");
        controller.attendToEvent(event);
        participantsNumberLabel.setText(event.getParticipants().size() + " people attending");

    }

    private void onEventCardClicked(MouseEvent mouseEvent) {
        changeToEventPageScreen();
    }

    private void changeToEventPageScreen() {
        try{
            sceneManager.changeToEventPageScene();
            sceneManager.centerStageOnScreen();
            EventPageController eventPageController = sceneManager.getEventPageController();
            eventPageController.setSceneManager(sceneManager);
            eventPageController.setController(controller);
            eventPageController.initializeEventPage(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        participantsNumberLabel.setText(event.getParticipants().size() + " people attending");
    }

    private void loadEventImage() {
        String pictureName = event.getTitle() + ".jpeg";

        String pictureURL = String.valueOf(MainApplication.class.getResource("assets/" + pictureName));
        if (pictureURL.equals("null")) {
            pictureURL = String.valueOf(MainApplication.class.getResource("assets/concert.jpeg"));
        }
        eventImageView.setImage(new Image(pictureURL));
    }
}
