package com.example.chatr.controllers;

import com.example.chatr.Application;
import com.example.chatr.Page;
import com.example.chatr.domain.Account;
import com.example.chatr.domain.FriendshipRequest;
import com.example.chatr.domain.User;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
    Button FriendshipRequestsButton;
    @FXML
    Label TitleLabel;
    @FXML
    Button LogoutButton;
    @FXML
    Button eventsButton;
    @FXML
    Button settingsButton;

    private ServiceUserFriendship serviceUserFriendship;
    private ServiceMessage serviceMessage;
    private ServiceFriendshipRequest serviceFriendshipRequest;
    private ServiceAccount serviceAccount;
    private ServiceEvent serviceEvent;
    private Account account;
    private Page page;

    @FXML
    private Label LabelHello;
    @FXML
    private TableView<UserTable> table;
    @FXML
    private TableColumn<UserTable, String> c1;
    @FXML
    private TableColumn<UserTable, String> c2;
    @FXML
    private TableColumn<UserTable, String> c3;
    @FXML
    private TableColumn<UserTable, String> buttonCollumn;
    @FXML
    private TableColumn<UserTable, String> buttonCollumn1;
    @FXML
    private TextField SearchTextField;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private String dashboard_status; //helps to get graphical updates
    private ObservableList<UserTable> modelGrade = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        c1.setCellValueFactory(new PropertyValueFactory<UserTable, String>("c1"));
        c2.setCellValueFactory(new PropertyValueFactory<UserTable, String>("c2"));
        c3.setCellValueFactory(new PropertyValueFactory<UserTable, String>("date"));
        buttonCollumn.setCellValueFactory(new PropertyValueFactory<UserTable,String>("button1"));
        buttonCollumn1.setCellValueFactory(new PropertyValueFactory<UserTable,String>("button2"));
        c3.setVisible(false);
        dashboard_status = "Show friends";
    }



    //-----------------------------Actions-------------------------------------------------
    public void onFriendshipRequestsButtonClick(javafx.scene.input.MouseEvent mouseEvent) throws RepoException {
        dashboard_status = "Friendship request";
        TitleLabel.setText("Friendship request");
        c3.setVisible(true);
        modelGrade.clear();
        buttonCollumn1.setVisible(true);

        for (FriendshipRequest fr : page.getFriendshipRequests()) {
            if (fr.getStatus().equals("PENDING")&&fr.getSender().getId()!=account.getUser_id()) {
                Button auxButton=new Button("Accept");
                Button auxButton2=new Button("Decline");

                UserTable userTable = new UserTable(fr.getSender().getFirstName(), fr.getSender().getLastName(), fr.getDate(),auxButton,auxButton2);
                //----Added event hanlder for any buttton("ACCEPT")
                auxButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    try {
                        page.respondRequest("APPROVED",fr.getSender().getId());
                        onFriendshipRequestsButtonClick(null);//update table
                    } catch (RepoException ex) {
                        ex.printStackTrace();
                    }
                });
                //----Added event hanlder for any buttton("DECLINE")
                auxButton2.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    try {
                        page.respondRequest("REJECTED",fr.getSender().getId());
                        onFriendshipRequestsButtonClick(null);//update table
                    } catch (RepoException ex) {
                        ex.printStackTrace();
                    }
                });
                modelGrade.add(userTable);

            }
        }
        table.setItems(modelGrade);
        searchFilter();
        table.getSortOrder().add(c1);
    }

    public void onAddFriendsButtonClick(javafx.scene.input.MouseEvent mouseEvent) throws RepoException {
        dashboard_status = "Add friends";
        TitleLabel.setText("Add friends");
        c3.setVisible(false);
        buttonCollumn1.setVisible(false);
        modelGrade.clear();

        User currentUser = serviceUserFriendship.find_user_by_id(account.getUser_id());
        Collection<User> users = serviceUserFriendship.getUserNotFriends(currentUser);
        List<User> usersOrdered = users.stream().sorted(Comparator.comparing(User::getFirstName)).toList();
        for (User user : usersOrdered) {
            Button auxButton;
            boolean isSent=false;
            for(FriendshipRequest fr: page.getFriendshipRequests()){
                if(serviceUserFriendship.find_user_by_id(fr.getReceiver().getId()).equals(user)){
                    ArrayList<FriendshipRequest>friends=page.getFriendshipRequests();
                    page.setFriendshipRequests(friends);
                    isSent=true;
                    break;
                }
            }
            if(!isSent)
                auxButton=new Button("Add");
            else
                auxButton=new Button("Undo");
            UserTable ut = new UserTable(user.getFirstName(), user.getLastName(),auxButton);
            //----Added event hanlder for any buttton
            auxButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                if(auxButton.getText().equals("Add")) {
                    page.addFriends(user.getId());
                    try {
                        onAddFriendsButtonClick(null);
                    } catch (RepoException ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    page.deleteRequest(user.getId());
                    try {
                        onAddFriendsButtonClick(null);
                    } catch (RepoException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            modelGrade.add(ut);
        }
        table.setItems(modelGrade);
        searchFilter();
        table.getSortOrder().add(c1);
    }

    public void onShowFriendsButtonClick(MouseEvent mouseEvent) throws RepoException {
        dashboard_status = "Show friends";
        TitleLabel.setText("Your friends");
        c3.setVisible(false);
        buttonCollumn1.setVisible(true);
        modelGrade.clear();

        User currentUser = serviceUserFriendship.find_user_by_id(account.getUser_id());
        for(User user: page.getFriends()){
                Button auxButton=new Button("Delete");
                Button chatButton = new Button("Chat");
                UserTable table = new UserTable(user.getFirstName(),
                        user.getLastName(),auxButton,chatButton);
                auxButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    page.deleteFriend(user.getId());
                    try {
                        onShowFriendsButtonClick(null);
                    } catch (RepoException ex) {
                        ex.printStackTrace();
                    }
                });
                chatButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    try {
                        openChat(currentUser,user);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                modelGrade.add(table);
        }
        table.setItems(modelGrade);
        searchFilter();
        table.getSortOrder().add(c1);
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
            loginController.setServices(serviceAccount, serviceUserFriendship, serviceMessage, serviceFriendshipRequest,serviceEvent);
            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root, 400, 600);
            stage.setTitle("Login");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void onSettingsButtonClick(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("settings.fxml"));
        root = fxmlLoader.load();
        SettingsController settingsController = fxmlLoader.getController();
        settingsController.setServices(page, account);
        stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root, 600, 400);
        stage.setTitle("Settings");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void onEventsButtonClick(javafx.scene.input.MouseEvent mouseEvent)throws  IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("events.fxml"));
        root = fxmlLoader.load();
        EventsController eventsController = fxmlLoader.getController();
        eventsController.setServices(page);
        stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("events");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    public void onShowFriendsButtonEnter(MouseEvent mouseEvent){
        ShowFriendsButton.setStyle("-fx-background-color: #b3b3b3");
    }
    public void onShowFriendsButtonExit(MouseEvent mouseEvent){
        ShowFriendsButton.setStyle("-fx-background-color: CDCDCD");
    }

    public void onAddFriendsButtonEnter(MouseEvent mouseEvent){
        AddFriendsButton.setStyle("-fx-background-color: #b3b3b3");
    }
    public void onAddFriendsButtonExit(MouseEvent mouseEvent){
        AddFriendsButton.setStyle("-fx-background-color: CDCDCD");
    }

    public void onFriendshipRequestsButtonEnter(MouseEvent mouseEvent){
        FriendshipRequestsButton.setStyle("-fx-background-color: #b3b3b3");
    }
    public void onFriendshipRequestsButtonExit(MouseEvent mouseEvent){
        FriendshipRequestsButton.setStyle("-fx-background-color: CDCDCD");
    }

    public void onSettingsButtonEnter(MouseEvent mouseEvent){
        settingsButton.setStyle("-fx-background-color: #b3b3b3");
    }
    public void onSettingsButtonExit(MouseEvent mouseEvent){
        settingsButton.setStyle("-fx-background-color: CDCDCD");
    }

    public void onEventsButtonEnter(MouseEvent mouseEvent) {
        eventsButton.setStyle("-fx-background-color: #b3b3b3");
    }

    public void onEventsButtonExit(MouseEvent mouseEvent) {
        eventsButton.setStyle("-fx-background-color: CDCDCD");
    }

    public void onLogoutButtonEnter(MouseEvent mouseEvent){
        LogoutButton.setStyle("-fx-background-color: #b3b3b3");
    }
    public void onLogoutButtonExit(MouseEvent mouseEvent){
        LogoutButton.setStyle("-fx-background-color: CDCDCD");
    }

    //----------------------------Functional--------------------------------------
    private void setServices() throws RepoException {
        this.account = page.getAccount();
        this.serviceUserFriendship = page.getServiceUserFriendship();
        this.serviceMessage = page.getServiceMessage();
        this.serviceFriendshipRequest = page.getServiceFriendshipRequest();
        this.serviceAccount=page.getServiceAccount();
        this.serviceEvent=page.getServiceEvent();
        User currentUser = serviceUserFriendship.find_user_by_id(account.getUser_id());
        LabelHello.setText("Hello, " + currentUser.getFirstName() + " " + currentUser.getLastName() + "!");
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


    private void openChat(User currentUser, User otherUser) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("chat.fxml"));
        Parent root2;
        Stage stage2 = new Stage();
        root2 = fxmlLoader.load();
        ChatController chatController = fxmlLoader.getController();
        chatController.setServices(serviceMessage,currentUser,otherUser);
        Scene scene2 = new Scene(root2, 396, 478);
        stage2.setTitle("Chat with " + otherUser.getFirstName() + " " + otherUser.getLastName());
        stage2.setResizable(false);
        stage2.setScene(scene2);
        stage2.show();
    }

    public void setPage(Page page) throws RepoException {
        this.page= page;
        setServices();
    }
}
