package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.service.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CreateEventPageController {
    private Controller controller;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextField locationTextField;

    @FXML
    private TextField timeTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Button createEventButton;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void onCreateEventButtonClicked(ActionEvent actionEvent) {
        createEvent();
        emptyTextFields();
    }

    private void createEvent() {
        String title = titleTextField.getText();
        String location = locationTextField.getText();
        String timeStr = timeTextField.getText();
        String description = descriptionTextArea.getText();
        LocalDate date = datePicker.getValue();

        if(title.isEmpty() || title.isBlank() || location.isEmpty() || location.isBlank() ||
        timeStr.isEmpty() || timeStr.isBlank() || description.isEmpty() || description.isBlank() || date == null) {
            return;
        }

        String[] hourMinutes = timeStr.split(":");
        if(hourMinutes.length !=2) {
            return;
        }

        LocalTime time = LocalTime.of(Integer.parseInt(hourMinutes[0]), Integer.parseInt(hourMinutes[1]));
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        controller.createEvent(title, description, location, dateTime);
    }

    private void emptyTextFields() {
        titleTextField.clear();
        timeTextField.clear();
        datePicker.setValue(null);
        locationTextField.clear();
        descriptionTextArea.clear();
    }
}
