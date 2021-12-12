package com.example.chatr;

import controllers.LoginController;
import domain.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.Repo;
import repository.database.*;
import service.ServiceAccount;
import service.ServiceFriendshipRequest;
import service.ServiceMessage;
import service.ServiceUserFriendship;
import validators.AccountValidator;
import validators.FriendshipRequestValidator;
import validators.FriendshipValidator;
import validators.UserValidator;

import java.io.IOException;

public class Aplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Repo<Integer, User> userRepo = new DbRepoUser("jdbc:postgresql://localhost:5432/network",
                "postgres","postgres");
        Repo<Integer, Friendship> friendshipRepo = new DbRepoFriendship("jdbc:postgresql://localhost:5432/network",
                "postgres","postgres");
        Repo<Integer, FriendshipRequest> friendshipRequestRepo = new DbRepoFriendshipRequest(
                "jdbc:postgresql://localhost:5432/network", "postgres","postgres");
        Repo<Integer, Message> messageRepo = new DbRepoMessage("jdbc:postgresql://localhost:5432/network",
                "postgres","postgres", userRepo);
        Repo<Integer, Account> accountRepo = new DbRepoAccount("jdbc:postgresql://localhost:5432/network",
                "postgres","postgres");

        ServiceUserFriendship serviceUserFriendship = new ServiceUserFriendship(userRepo, friendshipRepo,
                UserValidator.getInstance(), FriendshipValidator.getInstance());
        ServiceMessage serviceMessage = new ServiceMessage(userRepo,messageRepo);
        ServiceFriendshipRequest serviceFriendshipRequest=new ServiceFriendshipRequest(userRepo,friendshipRequestRepo,
                FriendshipRequestValidator.getInstance());
        ServiceAccount serviceAccount=new ServiceAccount(userRepo,accountRepo, AccountValidator.getInstance());
        //UI ui = new UI(serviceUserFriendship, serviceMessage,serviceFriendshipRequest);
        //ui.run();
        FXMLLoader fxmlLoader = new FXMLLoader(Aplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage.setTitle("Login");
        LoginController loginController=fxmlLoader.getController();
        loginController.setServices(serviceAccount,serviceUserFriendship,serviceMessage,serviceFriendshipRequest);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}