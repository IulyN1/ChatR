package com.example.chatr.controllers;

import com.example.chatr.Application;
import com.example.chatr.Page;
import com.example.chatr.domain.*;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SettingsController {
    private ServiceUserFriendship serviceUserFriendship;
    private ServiceMessage serviceMessage;
    private Account currentAccount;
    private Page page;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    DatePicker dateStart1;
    @FXML
    DatePicker dateEnd1;
    @FXML
    DatePicker dateStart2;
    @FXML
    DatePicker dateEnd2;
    @FXML
    ComboBox<UserDTO> userList = new ComboBox<>();
    ObservableList<UserDTO> friends = FXCollections.observableArrayList();

    public void init(){
        try {
            List<User> friendList = serviceUserFriendship.getUserFriends(currentAccount.getUser_id()).stream().toList();
            for(User us: friendList){
                UserDTO user = new UserDTO(us.getId(),us.getFirstName(),us.getLastName());
                friends.add(user);
            }
            userList.setItems(friends);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setServices(Page page, Account acc) {
        this.serviceMessage = page.getServiceMessage();
        this.serviceUserFriendship = page.getServiceUserFriendship();
        this.currentAccount = acc;
        this.page = page;
        init();
    }

    public void onGenerateButton1Clicked(MouseEvent mouseEvent) throws RepoException {
        LocalDate dateStart = dateStart1.getValue();
        LocalDate dateEnd = dateEnd1.getValue();
        if(dateStart != null && dateEnd != null) {
            List<Friendship> friendships = serviceUserFriendship.get_all_friendships().stream().toList();
            List<Friendship> friendshipsReport = new ArrayList<>();

            for (Friendship fr : friendships) {
                if (fr.getUser1().getId().equals(currentAccount.getUser_id()) ||
                        fr.getUser2().getId().equals(currentAccount.getUser_id())) {
                    String friendDate = fr.getFriendshipDate().split(" ")[0];
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate date = LocalDate.parse(friendDate, format);
                    if (date.compareTo(dateStart) >= 0 && date.compareTo(dateEnd) <= 0) {
                        friendshipsReport.add(fr);
                    }
                }
            }

            List<Message> messages = serviceMessage.get_all_messages().stream().toList();
            List<Message> messagesReport = new ArrayList<>();
            User thisUser = serviceUserFriendship.find_user_by_id(currentAccount.getUser_id());
            for (Message msg : messages) {
                if (msg.getTo().contains(thisUser)) {
                    LocalDateTime dateMsg = msg.getDate();
                    LocalDate date = dateMsg.toLocalDate();
                    if (date.compareTo(dateStart) >= 0 && date.compareTo(dateEnd) <= 0) {
                        messagesReport.add(msg);
                    }
                }
            }

            System.out.println(friendshipsReport);
            System.out.println(messagesReport);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please input dates!");
            alert.setContentText("Press Ok to go back!");
            alert.showAndWait();
        }
    }

    public void onGenerateButton2Clicked(MouseEvent mouseEvent) throws Exception {
        LocalDate dateStart = dateStart2.getValue();
        LocalDate dateEnd = dateEnd2.getValue();
        UserDTO sender = userList.getValue();
        if(dateStart != null && dateEnd != null && sender != null) {

            List<Message> messages = serviceMessage.get_chat(currentAccount.getUser_id(),sender.getId()).stream().toList();
            List<Message> messagesReport = new ArrayList<>();
            User thisUser = serviceUserFriendship.find_user_by_id(currentAccount.getUser_id());
            for (Message msg : messages) {
                if (msg.getTo().contains(thisUser)) {
                    LocalDateTime dateMsg = msg.getDate();
                    LocalDate date = dateMsg.toLocalDate();
                    if (date.compareTo(dateStart) >= 0 && date.compareTo(dateEnd) <= 0) {
                        messagesReport.add(msg);
                    }
                }
            }

            System.out.println(messagesReport);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please input dates and user!");
            alert.setContentText("Press Ok to go back!");
            alert.showAndWait();
        }
    }

    public void onBackButtonClicked(MouseEvent mouseEvent) throws IOException, RepoException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("dashboard-utility.fxml"));
        root = fxmlLoader.load();
        stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        DashboardUtilityController dashboardUtilityController = fxmlLoader.getController();
        dashboardUtilityController.setPage(page);
        scene = new Scene(root);
        stage.setTitle("Menu");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
