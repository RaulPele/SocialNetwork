package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.MainApplication;
import com.pelr.socialnetwork_extins.SceneManager;
import com.pelr.socialnetwork_extins.domain.User;
import com.pelr.socialnetwork_extins.service.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class HomePageController {

    private SceneManager sceneManager;
    private Controller controller;

    @FXML
    private ImageView profileImageView;

    @FXML
    private AnchorPane rootLayout;

    @FXML
    private Button friendRequestsButton;

    @FXML
    private VBox contactsListPane;



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
        loadContactsList();
    }

    private void loadContactsList(){
        Iterable<User> users = controller.findAllUsers();
        users.forEach(user ->{
            Node contactView = createContactView(user);
            contactsListPane.getChildren().add(contactView);
        });
    }

    private Node createContactView(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("fxml/contact-view.fxml"));
            Parent contactView = loader.load();
            Label contactLabel = (Label) contactView.lookup("#contactLabel");
            contactLabel.setText(user.getFirstName() + " " + user.getLastName());

            ImageView contactImageView = (ImageView) contactView.lookup("#contactImageView");
            contactImageView.setImage(new Image(String.valueOf(MainApplication.class.getResource("assets/unknown_user.png"))));
            contactView.setId("contactView");

            return contactView;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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
}
