package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.MainApplication;
import com.pelr.socialnetwork_extins.domain.Message;
import com.pelr.socialnetwork_extins.service.ChatRoom;
import com.pelr.socialnetwork_extins.service.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.List;

public class ChatRoomController {

    private Controller controller;
    private ChatRoom chatRoom;

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
        HBox buttonsBox = new HBox();

        Label messageLabel = new Label(message.getMessage());

        messageBox.getChildren().add(messageLabel);

        messageBox.setSpacing(5);
        buttonsBox.setSpacing(3);

        if (message.getFrom().getEmail().equals(controller.getLoggedUser().getEmail())) {
            messageLabel.setId("sentMessageLabel");
        } else {
            messageLabel.setId("receivedMessageLabel");
        }

        return messageBox;
    }

    private void reply(Message replyTo) {
        String replyMessage = messageTextField.getText();
        if(replyMessage.isEmpty() || replyMessage.isBlank()) {
            return;
        }
        chatRoom.reply(replyMessage, replyTo.getID());
        //refreshScreen();
        addMessageToLayout(chatRoom.getLastMessage());
        messageTextField.clear();

    }

    private void replyToAll(Message replyTo) {
        String replyMessage = messageTextField.getText();
        if(replyMessage.isEmpty() || replyMessage.isBlank()) {
            return;
        }
        chatRoom.replyToAll(replyMessage, replyTo.getID());
        //refreshScreen();
        addMessageToLayout(chatRoom.getLastMessage());
        messageTextField.clear();
    }

    public void onSendButtonClicked(ActionEvent actionEvent) {
        String messageString = messageTextField.getText();
        if(messageString.isEmpty() || messageString.isBlank()){
            return;
        }

        chatRoom.send(messageString);
        //refreshScreen();
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
