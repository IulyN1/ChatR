package com.example.chatr.controllers;

import com.example.chatr.Application;
import com.example.chatr.domain.Account;
import com.example.chatr.domain.User;
import com.example.chatr.service.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CreateAccountController {
    @FXML
    private TextField UsernameTextField;
    @FXML
    private TextField FirstNameTextField;
    @FXML
    private TextField LastNameTextField;
    @FXML
    private PasswordField PasswordField;
    @FXML
    private PasswordField ConfirmPasswordField;
    @FXML
    private Label BackLabel;
    @FXML
    private TableColumn<EventsTable, String> c1;
    @FXML
    private TableColumn<EventsTable, String> c2;


    private ServiceAccount serviceAccount;
    private ServiceUserFriendship serviceUserFriendship;
    private ServiceMessage serviceMessage;
    private ServiceFriendshipRequest serviceFriendshipRequest;
    private ServiceEvent serviceEvent;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void onBackLoginClicked(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login.fxml"));
        root = fxmlLoader.load();
        LoginController loginController = fxmlLoader.getController();
        loginController.setServices(serviceAccount, serviceUserFriendship, serviceMessage, serviceFriendshipRequest,serviceEvent);
        stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root, 400, 600);
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.setScene(scene);
        Image img = new Image("logo.png");
        stage.getIcons().add(img);
        stage.show();
    }

    public void onSubmitButtonClick(MouseEvent mouseEvent) {
        String username = UsernameTextField.getText();
        String password = PasswordField.getText();
        String passwordConfirmation = ConfirmPasswordField.getText();
        String firstname = FirstNameTextField.getText();
        String lastname = LastNameTextField.getText();

        //add validator for password
        if (!passwordConfirmation.equals(password)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Password fields do not match!");
            alert.setContentText("Press Ok to go back!");
            alert.showAndWait();
        } else

            //add validator for user
            try {
                serviceAccount.verifyUniqueUsername(username);
                serviceUserFriendship.addUser(firstname, lastname);
                int maxId = 0;
                for(int i=0;i<5;i++){
                    List<User> users = serviceUserFriendship.getAllUsers().stream().toList();
                    for(User us:users){
                        if(us.getId()>maxId)
                            maxId = us.getId();
                    }
                }
                User newUser = serviceUserFriendship.findUserById(maxId);
                String hashedPassword = serviceAccount.hashPassword(username,password);
                Account account = new Account(username, hashedPassword, newUser.getId());
                System.out.println(account);
                serviceAccount.addAccount(account);

                //message
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Account successfully created!");
                alert.setContentText("Press Ok to go continue!");
                alert.showAndWait();

                //go back to login
                FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login.fxml"));
                root = fxmlLoader.load();
                LoginController loginController = fxmlLoader.getController();
                loginController.setServices(serviceAccount, serviceUserFriendship, serviceMessage, serviceFriendshipRequest,serviceEvent);
                stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                scene = new Scene(root, 400, 600);
                stage.setTitle("Login");
                stage.setResizable(false);
                stage.setScene(scene);
                Image img = new Image("logo.png");
                stage.getIcons().add(img);
                stage.show();

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(e.getMessage());
                alert.setContentText("Press Ok to go back!");
                alert.showAndWait();
            }
    }

    public void setServices(ServiceAccount serviceAccount, ServiceUserFriendship serviceUserFriendship,
                            ServiceMessage serviceMessage, ServiceFriendshipRequest serviceFriendshipRequest, ServiceEvent serviceEvent) {
        this.serviceAccount = serviceAccount;
        this.serviceUserFriendship = serviceUserFriendship;
        this.serviceMessage = serviceMessage;
        this.serviceFriendshipRequest = serviceFriendshipRequest;
        this.serviceEvent=serviceEvent;
    }
}
