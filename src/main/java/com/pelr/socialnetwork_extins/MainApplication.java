package com.pelr.socialnetwork_extins;

import com.pelr.socialnetwork_extins.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneManager sceneManager = new SceneManager(stage);

        sceneManager.changeToLoginScene();
        LoginController loginController = sceneManager.getLoginController();
        loginController.setSceneManager(sceneManager);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}