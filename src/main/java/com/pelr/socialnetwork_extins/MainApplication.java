package com.pelr.socialnetwork_extins;

import com.pelr.socialnetwork_extins.controllers.FriendRequestsPageController;
import com.pelr.socialnetwork_extins.controllers.LoginController;
import com.pelr.socialnetwork_extins.domain.Event;
import com.pelr.socialnetwork_extins.domain.Friendship;
import com.pelr.socialnetwork_extins.domain.Tuple;
import com.pelr.socialnetwork_extins.domain.validators.FriendshipValidator;
import com.pelr.socialnetwork_extins.domain.validators.UserValidator;
import com.pelr.socialnetwork_extins.repository.Repository;
import com.pelr.socialnetwork_extins.repository.database.EventsDBRepository;
import com.pelr.socialnetwork_extins.repository.database.FriendshipDBRepository;
import com.pelr.socialnetwork_extins.repository.database.MessageDBRepository;
import com.pelr.socialnetwork_extins.repository.database.UserDBRepository;
import com.pelr.socialnetwork_extins.service.*;
import com.pelr.socialnetwork_extins.utils.Credentials;
import com.pelr.socialnetwork_extins.utils.PasswordEncryptor;
import com.pelr.socialnetwork_extins.utils.PasswordVerifier;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MainApplication extends Application {

    private Controller controller;
    private NotificationsThread notificationsThread;

    @Override
    public void start(Stage stage) throws IOException {
        initializeController();
        notificationsThread = new NotificationsThread(controller);
        setOnCloseRequestHandler(stage);
        initializeView(stage);
        stage.show();
    }

    private void setOnCloseRequestHandler(Stage stage) {
        stage.setOnCloseRequest(event ->
                notificationsThread.stop());
    }

    private void initializeController() {
        UserDBRepository userRepository = new UserDBRepository(Credentials.DB_URL, Credentials.DB_USERNAME, Credentials.DB_PASSWORD, new UserValidator());
        Repository<Tuple<Long, Long>, Friendship> friendshipRepository = new FriendshipDBRepository(Credentials.DB_URL,
                Credentials.DB_USERNAME, Credentials.DB_PASSWORD, new FriendshipValidator());

        MessageDBRepository messageRepository = new MessageDBRepository(Credentials.DB_URL, Credentials.DB_USERNAME, Credentials.DB_PASSWORD);
        EventsDBRepository eventRepository = new EventsDBRepository(Credentials.DB_URL, Credentials.DB_USERNAME, Credentials.DB_PASSWORD);

        UserService userService = new UserService(userRepository);
        FriendshipService friendshipService = new FriendshipService(friendshipRepository);
        Authentication authentication = new Authentication(userRepository);
        MessagingService messagingService = new MessagingService(messageRepository);
        EventService eventService = new EventService(eventRepository);

        controller = new Controller(userService, friendshipService, authentication, messagingService, eventService);
    }

    private void initializeView(Stage stage) throws IOException{
        SceneManager sceneManager = new SceneManager(stage);

        sceneManager.changeToLoginScene();
        LoginController loginController = sceneManager.getLoginController();

        loginController.setSceneManager(sceneManager);
        loginController.setController(controller);
        loginController.setNotificationsThread(notificationsThread);

    }


    public static void main(String[] args) {
       launch();
    }
}