package com.pelr.socialnetwork_extins;

import com.pelr.socialnetwork_extins.controllers.FriendRequestsPageController;
import com.pelr.socialnetwork_extins.controllers.HomePageController;
import com.pelr.socialnetwork_extins.controllers.LoginController;
import com.pelr.socialnetwork_extins.controllers.RegisterController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    private Stage window;

    private LoginController loginController;
    private RegisterController registerController;
    private HomePageController homePageController;
    private FriendRequestsPageController friendRequestsPageController;

    public SceneManager(Stage window) {
        this.window = window;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public RegisterController getRegisterController() {
        return registerController;
    }

    public HomePageController getHomePageController() {
        return homePageController;
    }

    public FriendRequestsPageController getFriendRequestsPageController() {
        return friendRequestsPageController;
    }

    public void changeToLoginScene() throws IOException {
        Scene loginScene = initLoginScene();
        window.setScene(loginScene);
    }

    private Scene initLoginScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/login-view.fxml"));

        Scene loginScene = new Scene(fxmlLoader.load());
        loginController = fxmlLoader.getController();
        window.setTitle("Login");

        return loginScene;
    }

    public void changeToRegisterScene() throws IOException {
        Scene registerScene = initRegisterScene();
        window.setScene(registerScene);
    }

    private Scene initRegisterScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/register-view.fxml"));
        Scene registerScene = new Scene(fxmlLoader.load());
        registerController = fxmlLoader.getController();
        window.setTitle("Create an account");

        return registerScene;
    }

    public void changeToHomePageScene() throws IOException {
        Scene homePageScene = initHomepageScene();
        window.setScene(homePageScene);
    }

    private Scene initHomepageScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/home_page-view.fxml"));
        Scene homePageScene = new Scene(fxmlLoader.load());
        homePageController = fxmlLoader.getController();
        window.setTitle("Social network");

        return homePageScene;
    }

    public void centerStageOnScreen(){
        this.window.centerOnScreen();
    }

    public void changeToFriendRequestsPageScene() throws IOException {
        Scene friendRequestsPageScene = initFriendRequestsPageScene();
        window.setScene(friendRequestsPageScene);
    }

    private Scene initFriendRequestsPageScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/friend_requests_page-view.fxml"));
        Scene friendRequestsPageScene = new Scene(fxmlLoader.load());
        friendRequestsPageController = fxmlLoader.getController();
        window.setTitle("Friend Requests");

        return friendRequestsPageScene;
    }
}