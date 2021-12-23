package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.SceneManager;
import com.pelr.socialnetwork_extins.service.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    private SceneManager sceneManager;
    private Controller controller;

    @FXML
    private Button loginButton;

    @FXML
    private Button createAccountButton;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordTextField;

    public void setController(Controller controller){
        this.controller = controller;
    }

    public void setSceneManager(SceneManager sceneManager){
        this.sceneManager = sceneManager;
    }

    public void onCreateAccountButtonClicked(ActionEvent actionEvent){
        try {
            sceneManager.changeToRegisterScene();
        } catch (IOException e){

            e.printStackTrace();
        }
    }
}
