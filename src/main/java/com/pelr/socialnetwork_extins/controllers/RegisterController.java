package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.SceneManager;
import com.pelr.socialnetwork_extins.domain.validators.ValidationException;
import com.pelr.socialnetwork_extins.service.Controller;
import com.pelr.socialnetwork_extins.service.UserAlreadyExistsException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.w3c.dom.Text;

import java.io.IOException;

public class RegisterController {

    private SceneManager sceneManager;
    private Controller controller;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button createAccountButton;

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    public void setSceneManager(SceneManager sceneManager){
        this.sceneManager = sceneManager;
    }

    public void onCreateAccountButtonClicked(ActionEvent actionEvent) {
        try{
            createAccount();
            changeToLoginScreen();
        } catch (ValidationException e) {
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
        } catch (UserAlreadyExistsException e){
            errorLabel.setVisible(true);
            errorLabel.setText("User already exists!");
        }
    }

    private void createAccount() throws ValidationException, UserAlreadyExistsException {
        String email = emailTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String password = passwordTextField.getText();

        if(password.isEmpty() || password.isBlank()) {
            throw new ValidationException("Invalid password!");
        }

        controller.addUser(firstName, lastName, email, password);

    }

    private void changeToLoginScreen() {
        try {
            sceneManager.changeToLoginScene();
            sceneManager.centerStageOnScreen();

            LoginController loginController = sceneManager.getLoginController();
            loginController.setSceneManager(sceneManager);
            loginController.setController(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onBackButtonClicked(ActionEvent actionEvent) {
        changeToLoginScreen();
    }
}
