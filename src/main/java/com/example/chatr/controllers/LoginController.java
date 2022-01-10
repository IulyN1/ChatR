package com.example.chatr.controllers;

import com.example.chatr.Application;
import com.example.chatr.Page;
import com.example.chatr.domain.Account;
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


public class LoginController {
    @FXML
    private Button LoginButton;
    @FXML
    private TextField UsernameTextField;
    @FXML
    private PasswordField PasswordField;
    @FXML
    private Label CreateAccountLabel;

    private ServiceAccount serviceAccount;
    private ServiceUserFriendship serviceUserFriendship;
    private ServiceMessage serviceMessage;
    private ServiceFriendshipRequest serviceFriendshipRequest;
    private ServiceEvent serviceEvent;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void onCreateAccountLabelEntered(MouseEvent mouseEvent) throws IOException {
        CreateAccountLabel.setStyle("-fx-underline: true;");
    }

    public void onCreateAccountLabelExited(MouseEvent mouseEvent) throws IOException {
        CreateAccountLabel.setStyle("-fx-underline: false");
    }

    public void onCreateAccountLabelClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("create-account.fxml"));
        root = fxmlLoader.load();
        stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        CreateAccountController createAccountController = fxmlLoader.getController();
        createAccountController.setServices(serviceAccount, serviceUserFriendship, serviceMessage, serviceFriendshipRequest,serviceEvent);
        scene = new Scene(root, 400, 600);
        stage.setTitle("Create account");
        stage.setResizable(false);
        stage.setScene(scene);
        Image img = new Image("logo.png");
        stage.getIcons().add(img);
        stage.show();
    }

    public void onLoginButtonClick(MouseEvent mouseEvent) throws IOException {
        String username = UsernameTextField.getText();
        String password = PasswordField.getText();
        try {
            serviceAccount.verifyAccount(username, password);
            Account account = serviceAccount.verifyAccount(username, password);
            System.out.println(account);
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("dashboard-utility.fxml"));
            root = fxmlLoader.load();
            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            DashboardUtilityController dashboardUtilityController = fxmlLoader.getController();
            dashboardUtilityController.setPage(new Page(account,serviceUserFriendship,serviceMessage,serviceFriendshipRequest,serviceAccount,serviceEvent));
            scene = new Scene(root);
            stage.setTitle("Menu");
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
            System.out.println(e.getMessage());
        }
    }

    public void setServices(ServiceAccount serviceAccount, ServiceUserFriendship serviceUserFriendship, ServiceMessage serviceMessage, ServiceFriendshipRequest serviceFriendshipRequest,ServiceEvent serviceEvent) {
        this.serviceAccount = serviceAccount;
        this.serviceUserFriendship = serviceUserFriendship;
        this.serviceMessage = serviceMessage;
        this.serviceFriendshipRequest = serviceFriendshipRequest;
        this.serviceEvent=serviceEvent;
    }
}