package controllers;

import com.example.chatr.Aplication;
import domain.Account;
import domain.FriendshipRequest;
import domain.User;
import exceptions.RepoException;
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
import javafx.stage.Stage;
import service.ServiceAccount;
import service.ServiceFriendshipRequest;
import service.ServiceMessage;
import service.ServiceUserFriendship;

import java.io.IOException;


public class DashboardUtilityController {
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
    private TableColumn<UserTable,Integer>c1;
    @FXML
    private TableColumn<UserTable,String>c2;
    @FXML
    private TableColumn<UserTable,String>c3;
    @FXML
    private TableColumn<UserTable,String>c4;
    @FXML
    private TextField SearchTextField;
    @FXML
    private TextField IdTextField;
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

    private Stage stage;
    private Scene scene;
    private Parent root;

    private String dashboard_status;//helps to get graphical updates

    private ObservableList<UserTable> modelGrade= FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        c1.setCellValueFactory(new PropertyValueFactory<UserTable,Integer>("id"));
        c2.setCellValueFactory(new PropertyValueFactory<UserTable,String>("c1"));
        c3.setCellValueFactory(new PropertyValueFactory<UserTable,String>("c2"));
        c4.setCellValueFactory(new PropertyValueFactory<UserTable,String>("date"));
        c4.setVisible(false);
        dashboard_status="Add friends";
        DeclineButton.setVisible(false);
    }

    public void onSendButtonClick(javafx.scene.input.MouseEvent mouseEvent) throws Exception {
        if(dashboard_status.equals("Add friends")){
            addFriends();
        }
        else if(dashboard_status.equals("Friendship request")){
            respondRequest("APPROVED");
        }
    }

    public void onDeclineButtonClick(javafx.scene.input.MouseEvent mouseEvent)throws Exception{
        respondRequest("REJECTED");
    }

    public void onFriendshipRequestsButtonClick(javafx.scene.input.MouseEvent mouseEvent)throws IOException{
        dashboard_status="Friendship request";
        TitleLabel.setText("Friendship request");
        SendRequestButton.setText("Accept");
        c4.setVisible(true);
        DeclineButton.setVisible(true);
        modelGrade.clear();
        for(FriendshipRequest fr:serviceFriendshipRequest.getAllRequests()){
            if(fr.getReceiver().getId()==account.getUser_id()&&fr.getStatus().equals("PENDING")){
                System.out.println(fr);
                UserTable userTable=new UserTable(fr.getSender().getId(),fr.getSender().getFirstName(),fr.getSender().getLastName(),fr.getDate());
                modelGrade.add(userTable);
            }
        }
        table.setItems(modelGrade);
        searchFilter();
    }

    public void onAddFriendsButtonClick(javafx.scene.input.MouseEvent mouseEvent) throws RepoException {
        dashboard_status="Add friends";
        TitleLabel.setText("Add friends");
        SendRequestButton.setText("Send Request");
        c4.setVisible(false);
        DeclineButton.setVisible(false);
        modelGrade.clear();
        for(User user:serviceUserFriendship.get_all_users()){
            User currentUser=serviceUserFriendship.find_user_by_id(account.getUser_id());
            if(!currentUser.equals(user)) {
                UserTable ut = new UserTable(user.getId(), user.getFirstName(), user.getLastName());
                modelGrade.add(ut);
            }
        }
        table.setItems(modelGrade);
        searchFilter();
    }

    public void onLogutButtonClick(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Yoy're about to logout!");
        alert.setContentText("Are you sure? ");
        if(alert.showAndWait().get()==ButtonType.OK){
            FXMLLoader fxmlLoader = new FXMLLoader(Aplication.class.getResource("login.fxml"));
            root=fxmlLoader.load();
            LoginController loginController=fxmlLoader.getController();
            loginController.setServices(serviceAccount,serviceUserFriendship,serviceMessage,serviceFriendshipRequest);
            stage=(Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
            scene=new Scene(root,400,600);
            stage.setTitle("Login");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }

    }
    public void setServices(ServiceAccount serviceAccount, ServiceUserFriendship serviceUserFriendship, ServiceMessage serviceMessage,
                            ServiceFriendshipRequest serviceFriendshipRequest,Account account) throws RepoException {
        this.serviceAccount=serviceAccount;
        this.serviceUserFriendship=serviceUserFriendship;
        this.serviceMessage=serviceMessage;
        this.serviceFriendshipRequest=serviceFriendshipRequest;
        this.account=account;
        LabelHello.setText("Hello "+account.getUsername());
        //-----initialize addFriendsDashboard------------
        onAddFriendsButtonClick(null);
    }

    private void searchFilter(){
        //---------search filter----------------------
        FilteredList<UserTable> filteredData=
                new FilteredList<>(modelGrade,b->true);
        SearchTextField.textProperty().addListener((observable ,oldValue,newValue)->
                filteredData.setPredicate(UserTable-> {
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
        SortedList<UserTable>sortedData=new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    private void addFriends(){
        try{
            int receiver_id=Integer.parseInt(IdTextField.getText());
            serviceFriendshipRequest.addFriendshipRequest(account.getUser_id(),receiver_id);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succes!");
            alert.setContentText("Friendship request sent!");
            alert.showAndWait();
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.setContentText("Press Ok to go back!");
            alert.showAndWait();

        }
    }

    private void respondRequest(String status)throws Exception{
        try{
            int id=Integer.parseInt(IdTextField.getText());
            serviceUserFriendship.find_user_by_id(id);//validate id
            for(FriendshipRequest fr:serviceFriendshipRequest.getAllRequests()){
                if(fr.getSender().getId()==id&&fr.getReceiver().getId()==account.getUser_id()){
                        fr.setStatus(status);
                        serviceFriendshipRequest.updateFriendshipRequest(fr);
                        if(status.equals("APPROVED"))
                            serviceUserFriendship.add_friendship(fr.getSender().getId(),fr.getReceiver().getId());
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Succes!");
                        if(status.equals("REJECTED"))
                            alert.setContentText("Request rejected!");
                        if(status.equals("APPROVED"))
                            alert.setContentText("Request approved!");
                        alert.showAndWait();
                        onFriendshipRequestsButtonClick(null);//update table
                        break;
                }

            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.setContentText("Press Ok to go back!");
            alert.showAndWait();
        }

    }

}
