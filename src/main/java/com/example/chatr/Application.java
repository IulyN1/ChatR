package com.example.chatr;

import com.example.chatr.controllers.LoginController;
import com.example.chatr.domain.*;
import com.example.chatr.repository.Repo;
import com.example.chatr.repository.database.*;
import com.example.chatr.service.*;
import com.example.chatr.validators.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Repo<Integer, User> userRepo = new DbRepoUser("jdbc:postgresql://localhost:5432/socialnetwork",
                "postgres", "postgres");
        Repo<Integer, Friendship> friendshipRepo = new DbRepoFriendship("jdbc:postgresql://localhost:5432/socialnetwork",
                "postgres", "postgres");
        Repo<Integer, FriendshipRequest> friendshipRequestRepo = new DbRepoFriendshipRequest(
                "jdbc:postgresql://localhost:5432/socialnetwork", "postgres", "postgres");
        Repo<Integer, Message> messageRepo = new DbRepoMessage("jdbc:postgresql://localhost:5432/socialnetwork",
                "postgres", "postgres", userRepo);
        Repo<Integer, Account> accountRepo = new DbRepoAccount("jdbc:postgresql://localhost:5432/socialnetwork",
                "postgres", "postgres");
        Repo<Integer, Event> eventRepo = new DbRepoEvent("jdbc:postgresql://localhost:5432/socialnetwork",
                "postgres", "postgres");
        ServiceUserFriendship serviceUserFriendship = new ServiceUserFriendship(userRepo, friendshipRepo,
                UserValidator.getInstance(), FriendshipValidator.getInstance());
        ServiceMessage serviceMessage = new ServiceMessage(userRepo, messageRepo);
        ServiceFriendshipRequest serviceFriendshipRequest = new ServiceFriendshipRequest(userRepo, friendshipRequestRepo,
                FriendshipRequestValidator.getInstance());
        ServiceAccount serviceAccount = new ServiceAccount(userRepo, accountRepo, AccountValidator.getInstance());
        ServiceEvent serviceEvent=new ServiceEvent(eventRepo, EventValidator.getInstance());
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage.setTitle("Login");
        LoginController loginController = fxmlLoader.getController();
        loginController.setServices(serviceAccount, serviceUserFriendship, serviceMessage, serviceFriendshipRequest,serviceEvent);
        stage.setResizable(false);
        stage.setScene(scene);
        Image img = new Image("logo.png");
        stage.getIcons().add(img);
        stage.show();

    }
}