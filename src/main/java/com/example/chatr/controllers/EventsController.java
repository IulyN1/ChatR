package com.example.chatr.controllers;

import com.example.chatr.service.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class EventsController {
    @FXML
    Button CreateEventButton;

    @FXML
    Button ShowEventsButton;

    @FXML
    Button BackButton;


    private ServiceAccount serviceAccount;
    private ServiceEvent serviceEvent;

    public void onCreateEventButtonEnter(MouseEvent mouseEvent){
        CreateEventButton.setStyle("-fx-background-color: #b3b3b3");
    }
    public void onCreateEventButtonExit(MouseEvent mouseEvent){
        CreateEventButton.setStyle("-fx-background-color: CDCDCD");
    }

    public void onShowEventsButtonEnter(MouseEvent mouseEvent){
        ShowEventsButton.setStyle("-fx-background-color: #b3b3b3");
    }
    public void onAddFriendsButtonExit(MouseEvent mouseEvent){
        ShowEventsButton.setStyle("-fx-background-color: CDCDCD");
    }

    public void onBackButtonEnter(MouseEvent mouseEvent){
        BackButton.setStyle("-fx-background-color: #b3b3b3");
    }
    public void onBackButtonExit(MouseEvent mouseEvent){
        BackButton.setStyle("-fx-background-color: CDCDCD");
    }


    public void setServices(ServiceAccount serviceAccount, ServiceEvent serviceEvent) {
        this.serviceAccount=serviceAccount;
        this.serviceEvent=serviceEvent;
    }
}
