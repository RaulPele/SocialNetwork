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
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class EventPageController implements Observer {
    private Controller controller;
    private SceneManager sceneManager;
    private Event event;

    @FXML
    private Label dateLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label numberOfParticipantsLabel;

    @FXML
    private Label creatorNameLabel;

    @FXML
    private Label detailsLocationLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button homeButton;

    @FXML
    private ImageView eventImageView;

    @FXML
    private Button attendButton;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void initializeEventPage(Event event) {
        this.event = event;

        dateLabel.setText(event.getDate().getDayOfWeek() + ", " + event.getDate().getMonth() + " " + event.getDate().getDayOfMonth() + " at " + event.getDate().getHour());
        titleLabel.setText(event.getTitle());
        locationLabel.setText(event.getLocation());
        numberOfParticipantsLabel.setText(event.getParticipants().size() + " people attending");

        creatorNameLabel.setText("Event created by: " + event.getCreator().getFirstName() + " " + event.getCreator().getLastName());
        detailsLocationLabel.setText(event.getLocation());
        descriptionLabel.setText(event.getDescription());

        Image image = new Image(String.valueOf(MainApplication.class.getResource("assets/concert.jpeg")));
        eventImageView.setImage(image);

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
        numberOfParticipantsLabel.setText(event.getParticipants().size() + " people attending");
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
}
