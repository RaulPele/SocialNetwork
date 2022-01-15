package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.MainApplication;
import com.pelr.socialnetwork_extins.domain.Message;
import com.pelr.socialnetwork_extins.service.ChatRoom;
import com.pelr.socialnetwork_extins.service.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.List;

public class ChatRoomController {

    private Controller controller;
    private ChatRoom chatRoom;
    private boolean replyModeOn;
    private Message repliedToMessage;

    @FXML
    private AnchorPane rootLayout;

    @FXML
    private AnchorPane chatRoomHeaderPane;

    @FXML
    private ScrollPane messagesScrollPane;

    @FXML
    private AnchorPane messageSendingPane;

    @FXML
    private Label nameLabel;

    @FXML
    private GridPane messagesPane;

    @FXML
    private Button sendButton;

    @FXML
    private TextField messageTextField;

    @FXML
    private TextField userListTextField;

    @FXML
    private Button sendToListButton;

    @FXML
    private ImageView contactHeaderImageView;

    private int messagesRowCount;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void initializeChatRoom(String email) {
        chatRoom = controller.createChatRoom(email);
        nameLabel.setText(chatRoom.getReceiver().getFirstName() + " " + chatRoom.getReceiver().getLastName());
        loadMessages();

        contactHeaderImageView.setImage(new Image(String.valueOf(MainApplication.class.getResource("assets/unknown_user.png"))));

        messagesPane.heightProperty().addListener((observable, oldValue, newValue) -> messagesScrollPane.setVvalue((Double) newValue));

    }

    private void loadMessages() {
        List<Message> messages = chatRoom.getMessages();
        messagesRowCount = 0;

        messages.forEach(this::addMessageToLayout);
    }

    private void addMessageToLayout(Message message) {
        Node messageView = createMessageView(message);

        if(message.getFrom().getEmail().equals(controller.getLoggedUser().getEmail())) {
            messagesPane.add(messageView, 0, messagesRowCount);
        } else {
            messagesPane.add(messageView, 2, messagesRowCount);
        }

        messagesRowCount++;
       // messagesPane.addRow(messagesRowCount);
    }

    private Node createMessageView(Message message) {
        VBox messageBox = new VBox();
        HBox messageAndReplyBox = new HBox();

        ImageView replyButton = new ImageView(new Image(String.valueOf(MainApplication.class.getResource("assets/reply-button.png"))));
        Label messageLabel = new Label(message.getMessage());
        Label repliedToLabel = new Label();

        replyButton.setVisible(false);

        if(message.getRepliedTo() != null) {
            Message repliedTo = message.getRepliedTo();
            repliedToLabel.setText(repliedTo.getMessage());
            repliedToLabel.setId("repliedToLabel");
            messageBox.getChildren().add(repliedToLabel);
        }

        if (message.getFrom().getEmail().equals(controller.getLoggedUser().getEmail())) {
            messageLabel.setId("sentMessageLabel");

            messageAndReplyBox.getChildren().add(messageLabel);
            messageAndReplyBox.getChildren().add(replyButton);
            VBox.setMargin(repliedToLabel, new Insets(0, 5, 0 ,0));
        } else {
            messageLabel.setId("receivedMessageLabel");
            messageBox.setAlignment(Pos.TOP_RIGHT);
            messageAndReplyBox.setAlignment(Pos.TOP_RIGHT);
            messageAndReplyBox.getChildren().add(replyButton);
            messageAndReplyBox.getChildren().add(messageLabel);
            VBox.setMargin(repliedToLabel, new Insets(0, 0, 0 ,5));
        }

        messageBox.getChildren().add(messageAndReplyBox);
        messageAndReplyBox.setSpacing(3);

        messageAndReplyBox.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> replyButton.setVisible(true));
        messageAndReplyBox.addEventHandler(MouseEvent.MOUSE_EXITED, event -> replyButton.setVisible(false));
        replyButton.setOnMouseClicked(e -> {
            changeReplyMode(message);
        });

        return messageBox;
    }

    private void changeReplyMode (Message message){
        if(replyModeOn) {
            exitReplyMode();
        }else{
            enterReplyMode(message);
        }
    }

    private void exitReplyMode() {
        if(replyModeOn) {
            replyModeOn= false;
            repliedToMessage = null;

            Node replyPopup = rootLayout.lookup("#replyPopup");
            rootLayout.getChildren().remove(replyPopup);
        }
    }

    private void enterReplyMode(Message message) {
        VBox replyPopup = new VBox();
        replyPopup.setId("replyPopup");
        replyPopup.setLayoutX(35);
        replyPopup.setLayoutY(475);
        Label replyPopupLabel = new Label(message.getMessage());
        replyPopupLabel.setId("replyPopupLabel");

        replyPopup.getChildren().add(replyPopupLabel);
        rootLayout.getChildren().add(replyPopup);
        replyPopup.toFront();

        repliedToMessage = message;
        replyModeOn = true;
    }

    private void reply(Message replyTo) {
        String replyMessage = messageTextField.getText();
        if(replyMessage.isEmpty() || replyMessage.isBlank()) {
            return;
        }
        chatRoom.reply(replyMessage, replyTo.getID());
    }

    private void replyToAll(Message replyTo) {
        String replyMessage = messageTextField.getText();
        if(replyMessage.isEmpty() || replyMessage.isBlank()) {
            return;
        }
        chatRoom.replyToAll(replyMessage, replyTo.getID());
    }

    public void onSendButtonClicked(ActionEvent actionEvent) {
        String messageString = messageTextField.getText();
        if(messageString.isEmpty() || messageString.isBlank()){
            return;
        }

        if(replyModeOn){
            reply(repliedToMessage);
            exitReplyMode();
        } else {
            chatRoom.send(messageString);
        }
        addMessageToLayout(chatRoom.getLastMessage());
        messageTextField.clear();
    }

    private void refreshScreen() {
        messagesPane.getChildren().clear();
        loadMessages();
    }

    public void onSendToListButtonClicked(ActionEvent actionEvent) {
        String userNames = userListTextField.getText();
        String messageString = messageTextField.getText();
        if(messageString.isEmpty() || messageString.isBlank() || userNames.isBlank() || userNames.isEmpty()){
            return;
        }

        try {
            chatRoom.sendToMultipleUsers(messageString, userNames);
            //refreshScreen();
            addMessageToLayout(chatRoom.getLastMessage());
            messageTextField.clear();
        } catch (Exception ex) {
            userListTextField.setText("Invalid list of users!");
        }
    }

}
