package com.pelr.socialnetwork_extins.controls;

import com.pelr.socialnetwork_extins.domain.User;
import com.pelr.socialnetwork_extins.service.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AutoCompleteTextField extends TextField {
    private List<User> users;
    private ContextMenu suggestionsPopup;
    private Controller controller;

    private static final int MAX_ENTRIES = 10;

    public AutoCompleteTextField() {
        super();
        users = new ArrayList<>();
        suggestionsPopup = new ContextMenu();
        suggestionsPopup.setId("suggestionsPopup");
        configureTextListener();
        configureFocusListener();
    }

    public void setController(Controller controller) {

    }

    private void configureTextListener(){

        Predicate<User> nameStartsWith = user -> {
            String firstName = user.getFirstName();
            String lastName = user.getLastName();
            String fullName = firstName + " " + lastName;
            String reverseFullName = lastName + " " + firstName;

            String searchedName = getText();

            return firstName.toLowerCase().startsWith(searchedName) ||
                    lastName.toLowerCase().startsWith(searchedName) ||
                    fullName.toLowerCase().startsWith(searchedName) ||
                    reverseFullName.toLowerCase().startsWith(searchedName);

             };
        textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(getText().length() == 0) {
                    suggestionsPopup.hide();
                } else{
                    List<User> searchResults = users.stream()
                            .filter(nameStartsWith)
                            .collect(Collectors.toList());

                    if(!users.isEmpty()) {
                        populatePopup(searchResults);

                        if(!suggestionsPopup.isShowing()) {
                            suggestionsPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
                        }
                    } else {
                        suggestionsPopup.hide();
                    }
                }
            }
        });
    }

    private void populatePopup(List<User> searchResults) {
        int numberOfEntries = Math.min(searchResults.size(), MAX_ENTRIES);
        suggestionsPopup.getItems().clear();

        for(int i=0; i< numberOfEntries; i++) {
            User user = searchResults.get(i);
            Label userLabel = new Label(user.getFirstName() + " " + user.getLastName());
            userLabel.setId("userResultLabel");
            CustomMenuItem item = new CustomMenuItem(userLabel, true);
            item.setId("suggestionItem");
            suggestionsPopup.getItems().add(item);

            item.setOnAction(actionEvent -> {
                setText(user.getFirstName() + " "+ user.getLastName());
                suggestionsPopup.hide();
            });

        }
    }


   public void setUsers(List<User> users) {
        this.users=users;
   }

   private void configureFocusListener() {
       focusedProperty().addListener((observableValue, aBoolean, aBoolean2) -> suggestionsPopup.hide());
   }

}
