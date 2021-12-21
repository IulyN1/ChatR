package com.example.chatr.controllers;

import com.example.chatr.domain.Message;
import com.example.chatr.domain.User;
import com.example.chatr.service.ServiceMessage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;

public class ChatController {
    @FXML
    ImageView sendMessageButton;
    @FXML
    TextField messageInput;
    @FXML
    Label labelChat;
    @FXML
    ScrollPane scrollMessages;
    @FXML
    VBox messageBox;
    ListView<String> listView = new ListView<>(FXCollections.observableArrayList());
    private ObservableList<String> modelGrade = FXCollections.observableArrayList();

    private ServiceMessage serviceMessage;
    private User user1;
    private User user2;

    @FXML
    public void initialize(){
        messageBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollMessages.setVvalue((Double)newValue);
            }
        });
        messageInput.setStyle("-fx-background-radius: 20px;");
        scrollMessages.setStyle("-fx-background-color: transparent;");
    }

    public void showMessages() {
        try {
            messageBox.getChildren().clear();
            List<Message> messages = serviceMessage.get_chat(user1.getId(), user2.getId()).stream().toList();
            for(Message msg: messages){
                Text text = new Text(msg.getMessage());
                TextFlow textFlow = new TextFlow(text);
                if(msg.getFrom().getId().equals(user1.getId())){
                    HBox root = new HBox(10);
                    root.setAlignment(Pos.CENTER_RIGHT);
                    root.setPadding(new Insets(10));

                    textFlow.setStyle("-fx-color:rgb(239,242,255);" +
                            "-fx-background-color: rgb(15,125,242);" +
                            "-fx-background-radius: 20px;");
                    textFlow.setPadding(new Insets(5,10,5,10));
                    text.setFill(Color.color(0.934,0.945,0.996));
                    text.setFont(new Font("Arial Bold",14));

                    root.getChildren().add(textFlow);
                    messageBox.getChildren().add(root);
                }
                else{
                    HBox root = new HBox(10);
                    root.setAlignment(Pos.CENTER_LEFT);
                    root.setPadding(new Insets(10));

                    textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
                            "-fx-background-radius: 20px;");
                    textFlow.setPadding(new Insets(5,10,5,10));
                    text.setFont(new Font(15));

                    root.getChildren().add(textFlow);
                    messageBox.getChildren().add(root);
                }
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void setServices(ServiceMessage serviceMessage,User user1, User user2) {
        this.serviceMessage = serviceMessage;
        this.user1 = user1;
        this.user2 = user2;
        showMessages();
    }

    public void onSendMessageClicked(MouseEvent mouseEvent) {
        try {
            String msg = messageInput.getText();
            List<Integer> to = new ArrayList<>();
            to.add(user2.getId());
            serviceMessage.sendMessage(user1.getId(), to, msg);
            messageInput.clear();
            showMessages();
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.setContentText("Press Ok to go back!");
            alert.showAndWait();
        }
    }
}