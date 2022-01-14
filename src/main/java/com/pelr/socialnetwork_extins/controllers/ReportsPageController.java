package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.SceneManager;
import com.pelr.socialnetwork_extins.domain.DTOs.ReportItem;
import com.pelr.socialnetwork_extins.service.Controller;
import com.pelr.socialnetwork_extins.utils.PDFWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReportsPageController {
    private Controller controller;
    private SceneManager sceneManager;
    private List<ReportItem> report;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Spinner<Integer> startHourSpinner;

    @FXML
    private Spinner<Integer> startMinuteSpinner;

    @FXML
    private Spinner<Integer> endHourSpinner;

    @FXML
    private Spinner<Integer> endMinuteSpinner;

    @FXML
    private Button friendsAndMessagesButton;

    @FXML
    private TextArea reportTextArea;

    @FXML
    private TextField friendNameTextField;


    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void initializeScreen() {
        initializeSpinners();
        report = new ArrayList<>();
    }

    private void initializeSpinners() {
        SpinnerValueFactory<Integer> startHoursValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        SpinnerValueFactory<Integer> endHoursValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);

        SpinnerValueFactory<Integer> startMinutesValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        SpinnerValueFactory<Integer> endMinutesValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);

        startHourSpinner.setValueFactory(startHoursValueFactory);
        endHourSpinner.setValueFactory(endHoursValueFactory);
        startMinuteSpinner.setValueFactory(startMinutesValueFactory);
        endMinuteSpinner.setValueFactory(endMinutesValueFactory);
    }

    public void onHomeButtonClicked(ActionEvent actionEvent) {
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

    public void onFriendAndMessagesButtonClicked(ActionEvent actionEvent) {
        reportTextArea.clear();
        loadReport();
    }

    private void loadReport() {
        LocalDateTime startDateTime = getStartDateTime();
        LocalDateTime endDateTime = getEndDateTime();
        if(startDateTime == null || endDateTime == null) {
            return;
        }

        report = controller.getFriendsAndMessagesReport(startDateTime, endDateTime);
        report.forEach(reportItem -> {
            reportTextArea.appendText(reportItem.toString() +'\n' +'\n');
        });
    }

    private LocalDateTime getStartDateTime() {
        LocalDate startDate = startDatePicker.getValue();
        int startMinute = startMinuteSpinner.getValue();
        int startHour = startHourSpinner.getValue();

        if(startDate == null) {
            return null;
        }
        LocalTime startTime = LocalTime.of(startHour, startMinute);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);

        return startDateTime;
    }

    private LocalDateTime getEndDateTime() {
        LocalDate endDate = endDatePicker.getValue();
        int endMinute = endMinuteSpinner.getValue();
        int endHour = endHourSpinner.getValue();

        if(endDate == null) {
            return null;
        }
        LocalTime endTime = LocalTime.of(endHour, endMinute);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        return endDateTime;
    }
    public void onMessagesFromFriendButtonClicked(ActionEvent actionEvent) {
        reportTextArea.clear();
        loadMessagesFromFriendReport();
    }

    private void loadMessagesFromFriendReport(){
        LocalDateTime startDateTime = getStartDateTime();
        LocalDateTime endDateTime = getEndDateTime();
        String friendName = friendNameTextField.getText();
        String[] names = friendName.split(" ");

        if(startDateTime == null || endDateTime == null || friendName.isBlank() || friendName.isEmpty() || names.length !=2) {
            return;
        }

        report = controller.getMessagesFromFriendReport(names[0], names[1], startDateTime, endDateTime);
        if(report.size() == 0) {
            reportTextArea.appendText("No messages received from " + friendName +" in the selected time period.");
        }
        report.forEach(reportItem -> {
            reportTextArea.appendText(reportItem.toString() +'\n' +'\n');
        });
    }

    public void onSaveToPDFButtonClicked(ActionEvent actionEvent) {
        String fileName = "generated_report";
        PDFWriter pdfWriter = new PDFWriter(fileName);

        if(report.size() == 0) {
            return ;
        }
        List<String> content = new ArrayList<>();
        report.forEach(reportItem -> content.add(reportItem.toString()));
        pdfWriter.writeFile(content);
    }
}
