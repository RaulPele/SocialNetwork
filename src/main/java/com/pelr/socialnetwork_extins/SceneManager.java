package com.pelr.socialnetwork_extins;

import com.pelr.socialnetwork_extins.controllers.LoginController;
import com.pelr.socialnetwork_extins.controllers.RegisterController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    private Stage window;

    private LoginController loginController;
    private RegisterController registerController;

    public SceneManager(Stage window) {
        this.window = window;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public RegisterController getRegisterController() {
        return registerController;
    }

    public void changeScene(Scene scene, String title) {
        window.setTitle(title);
        window.setScene(scene);
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
}