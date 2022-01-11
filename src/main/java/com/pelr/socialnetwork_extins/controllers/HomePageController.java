package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.MainApplication;
import com.pelr.socialnetwork_extins.SceneManager;
import com.pelr.socialnetwork_extins.domain.DTOs.ConversationHeaderDTO;
import com.pelr.socialnetwork_extins.service.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class HomePageController {
    private SceneManager sceneManager;
    private Controller controller;

    private ObservableList<ConversationHeaderDTO> contacts;

    @FXML
    private ImageView profileImageView;

    @FXML
    private AnchorPane rootLayout;

    @FXML
    private Button friendRequestsButton;

    @FXML
    private VBox contactsListPane;

    @FXML
    private TextField contactsSearchTextField;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() throws IOException {

    }

    public void initializeScreen() {
        contacts = FXCollections.observableArrayList();
        loadContactsList();
        contactsSearchTextField.textProperty().addListener(event -> handleContactsFilter());
        contacts.addListener((ListChangeListener<ConversationHeaderDTO>) c -> showFilteredContacts());
    }

    private void loadContactsList(){
        Iterable<ConversationHeaderDTO> users = controller.getConversationHeaders();
        users.forEach(headerDTO ->{
            Node contactView = createContactView(headerDTO);
            contactsListPane.getChildren().add(contactView);
            contacts.add(headerDTO);
        });
    }

    private Node createContactView(ConversationHeaderDTO headerDTO) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/contact-view.fxml"));
            Parent contactView = loader.load();
            Label contactLabel = (Label) contactView.lookup("#contactLabel");
            contactLabel.setText(headerDTO.getReceiverFirstName() + " " + headerDTO.getReceiverLastName());

            ImageView contactImageView = (ImageView) contactView.lookup("#contactImageView");
            contactImageView.setImage(new Image(String.valueOf(MainApplication.class.getResource("assets/unknown_user.png"))));
            contactView.setId("contactView");

            contactView.setOnMouseClicked(event -> openChatRoomView(headerDTO));

            return contactView;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void openChatRoomView(ConversationHeaderDTO header) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/chat_room-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.setTitle(header.getReceiverFirstName() + " "+ header.getReceiverLastName());
            stage.centerOnScreen();
            stage.show();


            ChatRoomController chatRoomController = fxmlLoader.getController();
            chatRoomController.setController(controller);
            chatRoomController.initializeChatRoom(header.getReceiverEmail());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onFriendRequestsButtonClicked(ActionEvent actionEvent) {
        changeToFriendRequestsScreen();
    }

    private void changeToFriendRequestsScreen(){
        try {
            sceneManager.changeToFriendRequestsPageScene();
            sceneManager.centerStageOnScreen();
            FriendRequestsPageController friendRequestsPageController = sceneManager.getFriendRequestsPageController();
            friendRequestsPageController.setSceneManager(sceneManager);
            friendRequestsPageController.setController(controller);

            friendRequestsPageController.initializeScreen();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void onProfileButtonClicked(ActionEvent actionEvent) {
        changeToProfilePageScreen(controller.getLoggedUser().getEmail());
    }

    private void changeToProfilePageScreen(String email) {
        try {
            sceneManager.changeToProfilePageScene();
            sceneManager.centerStageOnScreen();

            ProfilePageController profilePageController = sceneManager.getProfilePageController();
            profilePageController.setSceneManager(sceneManager);
            profilePageController.setController(controller);

            profilePageController.initializeProfile(email);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void onLogoutButtonClicked(ActionEvent actionEvent) {
        controller.logout();
        changeToLoginScreen();
    }

    private void changeToLoginScreen() {
        try{
            sceneManager.changeToLoginScene();
            sceneManager.centerStageOnScreen();

            LoginController loginController = sceneManager.getLoginController();
            loginController.setSceneManager(sceneManager);
            loginController.setController(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleContactsFilter() {
        String searchInput = contactsSearchTextField.getText().toLowerCase();

        Predicate<ConversationHeaderDTO> nameStartsWith = contact -> contact.getReceiverFirstName().toLowerCase().startsWith(searchInput) ||
                contact.getReceiverLastName().toLowerCase().startsWith(searchInput);

        List<ConversationHeaderDTO> headers = new ArrayList<>();
        controller.getConversationHeaders().forEach(headers::add);

        contacts.setAll( headers.stream()
                .filter(nameStartsWith)
                .collect(Collectors.toList()));
    }

    private void showFilteredContacts() {
        contactsListPane.getChildren().remove(2, contactsListPane.getChildren().size());
        contacts.forEach(contact -> contactsListPane.getChildren().add(createContactView(contact)));
    }
}
