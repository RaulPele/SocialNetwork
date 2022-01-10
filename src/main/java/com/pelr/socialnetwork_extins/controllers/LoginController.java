package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.SceneManager;
import com.pelr.socialnetwork_extins.service.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize(){
        errorLabel.setVisible(false);
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    public void setSceneManager(SceneManager sceneManager){
        this.sceneManager = sceneManager;
    }

    public void onCreateAccountButtonClicked(ActionEvent actionEvent){
        changeToRegisterScreen();
    }

    private void changeToRegisterScreen(){
        try {
            sceneManager.changeToRegisterScene();
            RegisterController registerController = sceneManager.getRegisterController();
            registerController.setSceneManager(sceneManager);
            registerController.setController(controller);
        } catch (IOException ex){
            ex.printStackTrace();
        }

    }

    public void onLoginButtonClicked(ActionEvent actionEvent) {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        try {
            controller.login(email, password);
            //System.out.println("Logged in successfuly!");
            changeToHomePageScreen();
        } catch (Exception e) {
            errorLabel.setVisible(true);
            errorLabel.setText("Invalid email or password!");
        }
    }

    private void changeToHomePageScreen() {
        try{
            sceneManager.changeToHomePageScene();
            sceneManager.centerStageOnScreen();

            HomePageController homePageController = sceneManager.getHomePageController();
            homePageController.setSceneManager(sceneManager);
            homePageController.setController(controller);
            homePageController.initializeScreen();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
