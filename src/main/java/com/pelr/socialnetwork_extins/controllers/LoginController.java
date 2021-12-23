package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class LoginController {

    private SceneManager sceneManager;

    @FXML
    private Button loginButton;

    @FXML
    private Button createAccountButton;


    public void setController(){

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
