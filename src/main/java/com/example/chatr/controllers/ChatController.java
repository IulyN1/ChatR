package com.example.chatr.controllers;

import com.example.chatr.domain.Message;
import com.example.chatr.domain.User;
import com.example.chatr.service.ServiceMessage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.text.TextAlignment;
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

    private ServiceMessage serviceMessage;
    private User user1;
    private User user2;

    /**
     * Method that initializes the ChatController
     */
    @FXML
    public void initialize(){
        messageBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollMessages.setVvalue((Double)newValue);
            }
        });
        messageInput.setStyle("-fx-background-radius: 20px;");
        scrollMessages.setStyle("-fx-background-color: transparent;" +
                "-fx-background: transparent;");
    }

    /**
     * Method that shows the messages in a ScrollPane
     */
    public void showMessages() {
        try {
            messageBox.getChildren().clear();
            List<Message> messages = serviceMessage.getChat(user1.getId(), user2.getId()).stream().toList();

            for(Message msg: messages){
                // reply message elements
                Text repliedText = new Text();
                repliedText.setFont(new Font(10));
                repliedText.setStyle("-fx-font-style: italic;");
                TextFlow replied = new TextFlow();

                // message elements
                TextFlow message = new TextFlow();
                TextFlow messageText = new TextFlow();
                HBox root = new HBox(10);

                // check if message is a reply
                if(msg.getReply() != null){
                    repliedText.setText("Replied to: " + msg.getReply().getMessage() + '\n');
                    replied.getChildren().add(repliedText);
                    message.getChildren().add(replied);
                    Separator separator = new Separator();
                    separator.setMinWidth(310);
                    message.getChildren().add(separator);

                }
                Text text = new Text(msg.getMessage());
                messageText.getChildren().add(text);

                // if the current user sent the message align message to right
                if(msg.getFrom().getId().equals(user1.getId())){
                    root.setAlignment(Pos.CENTER_RIGHT);
                    root.setPadding(new Insets(10));

                    message.setStyle("-fx-background-color: rgb(15,125,242);" +
                            "-fx-background-radius: 20px;");
                    message.setPadding(new Insets(5,10,5,10));
                    text.setFill(Color.color(0.934,0.945,0.996));
                    repliedText.setFill(Color.color(0.934,0.945,0.996));
                    text.setFont(new Font("Arial Bold",14));
                    message.setTextAlignment(TextAlignment.RIGHT);
                }
                // else align message to left
                else{
                    // menu elements for replying to messages
                    MenuItem reply = new MenuItem("Reply");
                    MenuItem replyAll = new MenuItem("Reply to all");
                    Menu menu = new Menu("⋮");
                    menu.getItems().addAll(reply,replyAll);
                    MenuBar menuBar = new MenuBar(menu);

                    messageText.getChildren().add(menuBar);
                    menuBar.setStyle("-fx-padding: 0 1 0 1;" +
                            "-fx-spacing: 1;" +
                            "-fx-background-color: transparent;");
                    menuBar.getMenus().get(0).setStyle("-fx-padding:0;");
                    menu.hide();

                    // handlers for events on menu items
                    menuBar.setOnMouseEntered(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            menu.show();
                        }
                    });
                    reply.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            sendReplyMessage(msg.getId());
                        }
                    });
                    replyAll.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            sendReplyAllMessage(msg.getId());
                        }
                    });

                    root.setAlignment(Pos.CENTER_LEFT);
                    root.setPadding(new Insets(10));
                    message.setStyle("-fx-background-color: rgb(233,233,235);" +
                            "-fx-background-radius: 20px;");
                    message.setPadding(new Insets(5,10,5,10));
                    text.setFont(new Font(15));
                }
                message.getChildren().add(messageText);
                root.getChildren().add(message);
                messageBox.getChildren().add(root);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method that sets the services for ChatController
     * @param serviceMessage service for Message
     * @param user1 User - the current user
     * @param user2 User - the other user participating in the chat
     */
    public void setServices(ServiceMessage serviceMessage,User user1, User user2) {
        this.serviceMessage = serviceMessage;
        this.user1 = user1;
        this.user2 = user2;
        showMessages();
    }

    /**
     * Method that sends the message on the click of the send button
     * @param mouseEvent MouseEvent
     */
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

    /**
     * Method that sends a reply message
     * @param id int - the id of the message to which the user replies
     */
    public void sendReplyMessage(int id) {
        try {
            String msg = messageInput.getText();
            serviceMessage.sendReplyMessage(id, user1.getId(), msg);
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

    /**
     * Method that sends a reply all message
     * @param id int - the id of the message to which the user replies
     */
    public void sendReplyAllMessage(int id) {
        try {
            String msg = messageInput.getText();
            serviceMessage.sendReplyToAll(id, user1.getId(), msg);
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

    /**
     * Method that refreshes the messages
     * @param mouseEvent MouseEvent
     */
    public void onRefreshButtonClicked(MouseEvent mouseEvent) {
        showMessages();
    }
}