package com.example.chatr.controllers;

import com.example.chatr.Application;
import com.example.chatr.domain.*;
import com.example.chatr.exceptions.FriendshipRequestException;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.service.ServiceAccount;
import com.example.chatr.service.ServiceFriendshipRequest;
import com.example.chatr.service.ServiceMessage;
import com.example.chatr.service.ServiceUserFriendship;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;


public class DashboardUtilityController {
    @FXML
    Button ShowFriendsButton;
    @FXML
    Button AddFriendsButton;
    @FXML
    Button SendRequestButton;
    @FXML
    Button FriendshipRequestsButton;
    @FXML
    Button DeclineButton;
    @FXML
    Label TitleLabel;
    @FXML
    Button LogoutButton;

    private ServiceAccount serviceAccount;
    private ServiceUserFriendship serviceUserFriendship;
    private ServiceMessage serviceMessage;
    private ServiceFriendshipRequest serviceFriendshipRequest;
    private Account account;

    @FXML
    private Label LabelHello;
    @FXML
    private TableView<UserTable> table;
    @FXML
    private TableColumn<UserTable, Integer> c1;
    @FXML
    private TableColumn<UserTable, String> c2;
    @FXML
    private TableColumn<UserTable, String> c3;
    @FXML
    private TableColumn<UserTable, String> c4;
    @FXML
    private TableColumn<UserTable, String> buttonCollumn;
    @FXML
    private TextField SearchTextField;
    @FXML
    private TextField IdTextField;

    private ArrayList<Button>tableButtons=new ArrayList<Button>();
    private Stage stage;
    private Scene scene;
    private Parent root;

    private String dashboard_status; //helps to get graphical updates
    private ObservableList<UserTable> modelGrade = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        c1.setCellValueFactory(new PropertyValueFactory<UserTable, Integer>("id"));
        c2.setCellValueFactory(new PropertyValueFactory<UserTable, String>("c1"));
        c3.setCellValueFactory(new PropertyValueFactory<UserTable, String>("c2"));
        c4.setCellValueFactory(new PropertyValueFactory<UserTable, String>("date"));
        buttonCollumn.setCellValueFactory(new PropertyValueFactory<UserTable,String>("button1"));
//        c1.setVisible(false);//id collumn
        c4.setVisible(false);
        dashboard_status = "Show friends";
        DeclineButton.setVisible(false);
    }

    public void onSendButtonClick(javafx.scene.input.MouseEvent mouseEvent) throws Exception {
        /*
        switch (dashboard_status) {
            case "Add friends" -> addFriends();
            case "Friendship request" -> respondRequest("APPROVED");
            case "Show friends" -> deleteFriend();
        }
         */
    }

    public void onDeclineButtonClick(javafx.scene.input.MouseEvent mouseEvent) throws Exception {
        respondRequest("REJECTED");
    }

    public void onFriendshipRequestsButtonClick(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        dashboard_status = "Friendship request";
        TitleLabel.setText("Friendship request");
        SendRequestButton.setText("Accept");
        c4.setVisible(true);
        DeclineButton.setVisible(true);
        modelGrade.clear();
        for (FriendshipRequest fr : serviceFriendshipRequest.getAllRequests()) {
            if (fr.getReceiver().getId() == account.getUser_id() && fr.getStatus().equals("PENDING")) {
                System.out.println(fr);
                Button auxButton=new Button("Request");
                tableButtons.add(auxButton);
                UserTable userTable = new UserTable(fr.getSender().getId(), fr.getSender().getFirstName(), fr.getSender().getLastName(), fr.getDate(),auxButton);
                //----Added event hanlder for any buttton
                auxButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                            System.out.println("s-a apasat");
                        });
                        modelGrade.add(userTable);
            }
        }
        table.setItems(modelGrade);
        searchFilter();
    }

    public void onAddFriendsButtonClick(javafx.scene.input.MouseEvent mouseEvent) throws RepoException {
        dashboard_status = "Add friends";
        TitleLabel.setText("Add friends");
        SendRequestButton.setText("Send Request");
        c4.setVisible(false);
        DeclineButton.setVisible(false);
        modelGrade.clear();
        tableButtons.clear();
        User currentUser = serviceUserFriendship.find_user_by_id(account.getUser_id());
        Collection<User> users = serviceUserFriendship.getUserNotFriends(currentUser);
        List<User> usersOrdered = users.stream().sorted(Comparator.comparing(Entity::getId)).toList();
        for (User user : usersOrdered) {
            Button auxButton;
            auxButton=new Button("Add");
            for(FriendshipRequest friendshipRequest:serviceFriendshipRequest.getAllRequests()){
                if(friendshipRequest.getSender().getId()==account.getUser_id()&&
                friendshipRequest.getReceiver().getId()==user.getId())
                    auxButton.setText("Undo");

            }
            tableButtons.add(auxButton);
            UserTable ut = new UserTable(user.getId(), user.getFirstName(), user.getLastName(),auxButton);
            //----Added event hanlder for any buttton
            auxButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                System.out.println(ut.getId());
                addFriends(ut.getId());
            });
            modelGrade.add(ut);
        }
        table.setItems(modelGrade);
        searchFilter();
    }

    public void onShowFriendsButtonClick(MouseEvent mouseEvent) throws RepoException {
        dashboard_status = "Show friends";
        TitleLabel.setText("Your friends");
        SendRequestButton.setText("Delete");
        SendRequestButton.setVisible(true);
        c4.setVisible(false);
        DeclineButton.setVisible(false);
        modelGrade.clear();

        User currentUser = serviceUserFriendship.find_user_by_id(account.getUser_id());
        for(Friendship fr: serviceUserFriendship.get_all_friendships()){
            if(fr.getUser1().equals(currentUser)){
                Button auxButton=new Button("Delete");
                tableButtons.add(auxButton);
                UserTable user = new UserTable(fr.getUser2().getId(),fr.getUser2().getFirstName(),
                        fr.getUser2().getLastName(),auxButton);
                auxButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    System.out.println("s-a apasat");
                    System.out.println(user.getId());
                });
                modelGrade.add(user);
            }
            else if(fr.getUser2().equals(currentUser)){
                Button auxButton=new Button("Delete");
                tableButtons.add(auxButton);
                UserTable user = new UserTable(fr.getUser1().getId(),fr.getUser1().getFirstName(),
                        fr.getUser1().getLastName(),auxButton);
                auxButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    System.out.println("s-a apasat");
                    System.out.println(user.getId());
                });
                modelGrade.add(user);
            }
        }
        table.setItems(modelGrade);
        searchFilter();
    }

    public void onLogoutButtonClick(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Are you sure?");

        if (alert.showAndWait().get() == ButtonType.OK) {
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
    }


    public void setServices(ServiceAccount serviceAccount, ServiceUserFriendship serviceUserFriendship, ServiceMessage serviceMessage,
                            ServiceFriendshipRequest serviceFriendshipRequest, Account account) throws RepoException {
        this.serviceAccount = serviceAccount;
        this.serviceUserFriendship = serviceUserFriendship;
        this.serviceMessage = serviceMessage;
        this.serviceFriendshipRequest = serviceFriendshipRequest;
        this.account = account;
        LabelHello.setText("Hello " + account.getUsername());
        //-----initialize showFriendsDashboard------------
        onShowFriendsButtonClick(null);
    }

    private void searchFilter() {
        //---------search filter----------------------
        FilteredList<UserTable> filteredData =
                new FilteredList<>(modelGrade, b -> true);
        SearchTextField.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(UserTable -> {
                    if (newValue.isEmpty() || newValue.isBlank() ||
                            newValue == null) {
                        return true;
                    }
                    String searchKeyword = newValue.toLowerCase();
                    if (UserTable.getC1().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (UserTable.getC2().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else
                        return false;
                })
        );
        SortedList<UserTable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    private void addFriends(int receiver_id) {
        try {
//            int receiver_id = Integer.parseInt(IdTextField.getText());
            checkRequest(receiver_id);
            serviceFriendshipRequest.addFriendshipRequest(account.getUser_id(), receiver_id);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setContentText("Friendship request sent!");
            alert.showAndWait();
            onAddFriendsButtonClick(null);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.setContentText("Press Ok to go back!");
            alert.showAndWait();
        }
    }

    private void checkRequest(int userId) throws FriendshipRequestException {
        for (FriendshipRequest fr2 : serviceFriendshipRequest.getAllRequests()) {
            if (fr2.getSender().getId() == userId && fr2.getReceiver().getId() == account.getUser_id() &&
                    fr2.getStatus().equals("PENDING")) {
                throw new FriendshipRequestException("Already have a pending request from that user!");
            }
        }
    }

    private void respondRequest(String status) throws Exception {
        try {
            int id = Integer.parseInt(IdTextField.getText());
            serviceUserFriendship.find_user_by_id(id);//validate id
            for (FriendshipRequest fr : serviceFriendshipRequest.getAllRequests()) {
                if (fr.getSender().getId() == id && fr.getReceiver().getId() == account.getUser_id()) {
                    fr.setStatus(status);
                    serviceFriendshipRequest.updateFriendshipRequest(fr);
                    if (status.equals("APPROVED"))
                        serviceUserFriendship.add_friendship(fr.getSender().getId(), fr.getReceiver().getId());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success!");
                    if (status.equals("REJECTED"))
                        alert.setContentText("Request rejected!");
                    if (status.equals("APPROVED"))
                        alert.setContentText("Request approved!");
                    alert.showAndWait();
                    onFriendshipRequestsButtonClick(null);//update table
                    break;
                }

            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.setContentText("Press Ok to go back!");
            alert.showAndWait();
        }
    }

    private void deleteFriend() {
        try{
            User currentUser = serviceUserFriendship.find_user_by_id(account.getUser_id());
            int currentId = currentUser.getId();
            int otherId = Integer.parseInt(IdTextField.getText());

            // delete the friendship
            for(Friendship fr: serviceUserFriendship.get_all_friendships()){
                if(fr.getUser1().getId().equals(currentId) && fr.getUser2().getId().equals(otherId)){
                    serviceUserFriendship.delete_friendship(fr.getId());
                }
                else if(fr.getUser2().getId().equals(currentId) && fr.getUser1().getId().equals(otherId)){
                    serviceUserFriendship.delete_friendship(fr.getId());
                }
            }
            // delete the friendship request
            for(FriendshipRequest fr: serviceFriendshipRequest.getAllRequests()){
                if(fr.getReceiver().getId().equals(currentId) && fr.getSender().getId().equals(otherId)){
                    serviceFriendshipRequest.deleteFriendshipRequest(fr.getId());
                }
                else if(fr.getSender().getId().equals(currentId) && fr.getReceiver().getId().equals(otherId)){
                    serviceFriendshipRequest.deleteFriendshipRequest(fr.getId());
                }
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success!");
            alert.setContentText("Press OK to go back!");
            alert.showAndWait();
            onShowFriendsButtonClick(null);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.setContentText("Press OK to go back!");
            alert.showAndWait();
        }
    }
}
