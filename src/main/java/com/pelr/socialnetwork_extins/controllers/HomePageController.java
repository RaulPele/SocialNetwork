package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.MainApplication;
import com.pelr.socialnetwork_extins.SceneManager;
import com.pelr.socialnetwork_extins.controls.AutoCompleteTextField;
import com.pelr.socialnetwork_extins.domain.DTOs.ConversationHeaderDTO;
import com.pelr.socialnetwork_extins.domain.Event;
import com.pelr.socialnetwork_extins.domain.User;
import com.pelr.socialnetwork_extins.service.Controller;
import com.pelr.socialnetwork_extins.utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class HomePageController implements Observer {
    private SceneManager sceneManager;
    private Controller controller;

    private ObservableList<ConversationHeaderDTO> contacts;

    private int eventColumnCount = 0;
    private int eventRowCount = 0;

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

    @FXML
    private AutoCompleteTextField autoCompleteTextField;

    @FXML
    private GridPane homeEventsGridPane;

    @FXML
    private Button createEventButton;

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
        loadCustomSearchBar();
        loadEvents();
        controller.addObserver(this);
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

    private void loadCustomSearchBar() {
        List<User> users = new ArrayList<>();
        controller.findAllUsers().forEach(users::add);
        autoCompleteTextField.setUsers(users);
        autoCompleteTextField.setOnKeyPressed(this::handleSearchBarOnKeyPressed);
    }

    private void handleSearchBarOnKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            String searchedName = autoCompleteTextField.getText();
            if (searchedName.isEmpty() || searchedName.isBlank()) {
                return;
            }

            String[] names = searchedName.split(" ");

            if (names.length == 2) {
                String email = controller.findEmailByUserName(names[0], names[1]);
                changeToProfilePageScreen(email);
            }
        }
    }

    private void loadEvents() {
        Iterable<Event> events = controller.findAllEvents();
        events.forEach(event -> {
            Node eventCardView = createEventCardView(event);
            addEventCardToLayout(eventCardView);
        });

    }

    private Node createEventCardView(Event event) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/event_card-view.fxml"));
        try {
            Node eventCardView = fxmlLoader.load();
            EventCardController eventCardController = fxmlLoader.getController();
            eventCardController.setController(controller);
            eventCardController.setSceneManager(sceneManager);
            eventCardController.initializeEvent(event);

            return eventCardView;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void addEventCardToLayout(Node eventCardView) {
        homeEventsGridPane.add(eventCardView, eventColumnCount, eventRowCount);
        GridPane.setHalignment(eventCardView, HPos.CENTER);
        eventRowCount++;
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

        Predicate<ConversationHeaderDTO> nameStartsWith = contact -> {
            String firstName = contact.getReceiverFirstName();
            String lastName = contact.getReceiverLastName();
            String fullName = firstName + " " + lastName;
            String reverseFullName = lastName + " " + firstName;

            return firstName.toLowerCase().startsWith(searchInput) ||
                    lastName.toLowerCase().startsWith(searchInput) ||
                    fullName.toLowerCase().startsWith(searchInput) ||
                    reverseFullName.toLowerCase().startsWith(searchInput);
        };

//        List<ConversationHeaderDTO> headers = new ArrayList<>();
//        controller.getConversationHeaders().forEach(headers::add);

//        contacts.setAll( headers.stream()
//                .filter(nameStartsWith)
//                .collect(Collectors.toList()));

        Iterable<ConversationHeaderDTO> headerDTOS = controller.getConversationHeaders();
        contacts.setAll(StreamSupport.stream(headerDTOS.spliterator(), false)
                .filter(nameStartsWith)
                .collect(Collectors.toList()));
    }

    private void showFilteredContacts() {
        contactsListPane.getChildren().remove(2, contactsListPane.getChildren().size());
        contacts.forEach(contact -> contactsListPane.getChildren().add(createContactView(contact)));
    }

    public void onCreateEventButtonClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/create_event_page-view.fxml"));
            Scene createEventScene = new Scene(loader.load());
            Stage createEventStage = new Stage();
            createEventStage.setScene(createEventScene);

            CreateEventPageController createEventPageController = loader.getController();
            createEventPageController.setController(controller);

            createEventStage.show();
            createEventStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        refreshEventsList();
    }

    private void refreshEventsList() {
        homeEventsGridPane.getChildren().clear();
        eventRowCount=0;
        loadEvents();
    }

    public void onReportsButtonClicked(ActionEvent actionEvent) {
        changeToReportsScreen();
    }

    private void changeToReportsScreen() {
        try{
            sceneManager.changeToReportsPageScene();
            sceneManager.centerStageOnScreen();

            ReportsPageController reportsPageController = sceneManager.getReportsPageController();
            reportsPageController.setController(controller);
            reportsPageController.setSceneManager(sceneManager);
            reportsPageController.initializeScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
