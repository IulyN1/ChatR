package com.example.chatr.controllers;

import com.example.chatr.Application;
import com.example.chatr.domain.Account;
import com.example.chatr.domain.User;
import com.example.chatr.service.ServiceAccount;
import com.example.chatr.service.ServiceFriendshipRequest;
import com.example.chatr.service.ServiceMessage;
import com.example.chatr.service.ServiceUserFriendship;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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

    private ServiceAccount serviceAccount;
    private ServiceUserFriendship serviceUserFriendship;
    private ServiceMessage serviceMessage;
    private ServiceFriendshipRequest serviceFriendshipRequest;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void onBackLabelEntered(MouseEvent mouseEvent) {
        BackLabel.setStyle("-fx-underline: true;");
    }

    public void onBackLabelExited(MouseEvent mouseEvent) {
        BackLabel.setStyle("-fx-underline: false");
    }

    public void onBackLabelClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login.fxml"));
        root = fxmlLoader.load();
        LoginController loginController = fxmlLoader.getController();
        loginController.setServices(serviceAccount, serviceUserFriendship, serviceMessage, serviceFriendshipRequest);
        stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root, 400, 600);
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.setScene(scene);
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
                serviceUserFriendship.add_user(firstname, lastname);
                List<User> users = serviceUserFriendship.get_all_users().stream().toList();
                User newUser = users.get(users.size() - 1);
                Account account = new Account(username, password, newUser.getId());
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
                loginController.setServices(serviceAccount, serviceUserFriendship, serviceMessage, serviceFriendshipRequest);
                stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                scene = new Scene(root, 400, 600);
                stage.setTitle("Login");
                stage.setResizable(false);
                stage.setScene(scene);
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
                            ServiceMessage serviceMessage, ServiceFriendshipRequest serviceFriendshipRequest) {
        this.serviceAccount = serviceAccount;
        this.serviceUserFriendship = serviceUserFriendship;
        this.serviceMessage = serviceMessage;
        this.serviceFriendshipRequest = serviceFriendshipRequest;
    }
}
