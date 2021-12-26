package com.pelr.socialnetwork_extins.controllers;

import com.pelr.socialnetwork_extins.SceneManager;
import com.pelr.socialnetwork_extins.domain.validators.ValidationException;
import com.pelr.socialnetwork_extins.service.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.w3c.dom.Text;

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
        String email = emailTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String password = passwordTextField.getText();

        if(password.isEmpty() || password.isBlank()) {
            errorLabel.setVisible(true);
            errorLabel.setText("Invalid password!");
            return;
        }

        try {
            controller.addUser(firstName, lastName, email, password);
        } catch (ValidationException e) {
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
