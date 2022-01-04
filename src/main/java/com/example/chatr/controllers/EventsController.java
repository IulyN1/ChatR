package com.example.chatr.controllers;

import com.example.chatr.Application;
import com.example.chatr.Page;
import com.example.chatr.domain.Event;
import com.example.chatr.exceptions.EventException;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class EventsController {
    @FXML
    private Button CreateEventButton;
    @FXML
    private Button ShowEventsButton;
    @FXML
    private Button BackButton;
    @FXML
    private Label TitleLabel;
    @FXML
    private TextField EventNameLabel;
    @FXML
    private DatePicker DatePicker;
    @FXML
    private Button AddEventButton;
    @FXML
    private TextField SearchTextField;
    @FXML
    private ImageView SearchImage;
    @FXML
    private TableView table;
    @FXML
    private GridPane gridPane;
    @FXML
    private TableColumn<EventsTable, String> c1;
    @FXML
    private TableColumn<EventsTable, String> c2;
    @FXML
    private TableColumn<EventsTable, String> c3;
    @FXML
    private Button MyEventsButton;

    private ServiceAccount serviceAccount;
    private ServiceEvent serviceEvent;
    private Page page;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private ObservableList<EventsTable> modelGrade = FXCollections.observableArrayList();
    @FXML
    public void initialize() {
        c1.setCellValueFactory(new PropertyValueFactory<EventsTable, String>("c1"));
        c2.setCellValueFactory(new PropertyValueFactory<EventsTable, String>("c2"));
        c2.setCellValueFactory(new PropertyValueFactory<EventsTable, String>("button1"));
    }
    public void onCreateEventButtonClick(MouseEvent mouseEvent){
        TitleLabel.setText("Create Event");
        EventNameLabel.setVisible(true);
        DatePicker.setVisible(true);
        AddEventButton.setVisible(true);
        //---------------------------
        SearchTextField.setVisible(false);
        SearchImage.setVisible(false);
        table.setVisible(false);
        gridPane.setVisible(false);
    }

    public void onShowEventsButtonClick(MouseEvent mouseEvent){
        EventNameLabel.setVisible(false);
        DatePicker.setVisible(false);
        AddEventButton.setVisible(false);
        //------------------------------
        SearchTextField.setVisible(true);
        SearchImage.setVisible(true);
        table.setVisible(true);
        gridPane.setVisible(true);
        TitleLabel.setText("Show Events");
        modelGrade.clear();
        for(Event event:serviceEvent.getAllEvent()) {
            Boolean isEvent = false;
            for (Event event1 : page.getEvents())
                if (event.equals(event1)) {
                    isEvent = true;
                    break;
                }
            if (!isEvent) {
                Button auxButton = new Button("Subscribe");
                auxButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                            String id = String.valueOf(page.getAccount().getUser_id());
                            event.setSubscribers(event.getSubscribers() + " " + id);
                            ArrayList <Event>events=page.getEvents();
                            events.add(event);
                            page.setEvents(events);
                    try {
                        serviceEvent.updateEvent(event);
                        onShowEventsButtonClick(null);
                    } catch (Exception ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error!");
                        alert.setHeaderText(ex.getMessage());
                        alert.setContentText("Press Ok to go back!");
                        alert.showAndWait();
                    }
                        }
                );
                EventsTable eventsTable = new EventsTable(event.getName(), event.getDate(), auxButton);
                modelGrade.add(eventsTable);
            } else {
                Button auxButton = new Button("Unsubscribe");
                auxButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                            String id = String.valueOf(page.getAccount().getUser_id());
                            id=" "+id;
                            event.setSubscribers(event.getSubscribers().replace(id,""));
                            ArrayList <Event>events=page.getEvents();
                            events.remove(event);
                            page.setEvents(events);
                    try {
                        serviceEvent.updateEvent(event);
                        onShowEventsButtonClick(null);
                    } catch (Exception ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error!");
                        alert.setHeaderText(ex.getMessage());
                        alert.setContentText("Press Ok to go back!");
                        alert.showAndWait();
                    }
                        }

                );
                EventsTable eventsTable = new EventsTable(event.getName(), event.getDate(), auxButton);
                modelGrade.add(eventsTable);
            }
        }
        table.setItems(modelGrade);
        searchFilter();
        table.getSortOrder().add(c1);
    }


    public void onBackButtonClick(MouseEvent mouseEvent) throws IOException, RepoException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("dashboard-utility1.fxml"));
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


    public void onCreateEventButtonEnter(MouseEvent mouseEvent){
        CreateEventButton.setStyle("-fx-background-color: #b3b3b3");
    }
    public void onCreateEventButtonExit(MouseEvent mouseEvent){
        CreateEventButton.setStyle("-fx-background-color: CDCDCD");
    }

    public void onShowEventsButtonEnter(MouseEvent mouseEvent){
        ShowEventsButton.setStyle("-fx-background-color: #b3b3b3");
    }
    public void onShowEventsButtonExit(MouseEvent mouseEvent){
        ShowEventsButton.setStyle("-fx-background-color: CDCDCD");
    }



    public void onAddEventButtonClick(MouseEvent mouseEvent) throws Exception{
        String EventName=EventNameLabel.getText();
        LocalDate date=DatePicker.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String formattedString = date.format(formatter);
        Event event=new Event(EventName,formattedString," ");
        try {
            serviceEvent.addEvent(event);
            //alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setContentText("Event added successfully!");
            alert.showAndWait();
            EventNameLabel.clear();
        }catch (EventException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText(e.getMessage());
            alert.setContentText("Press Ok to go back!");
            alert.showAndWait();
        }
        //add the creator
        /*
        String id = String.valueOf(page.getAccount().getUser_id());
        event.setSubscribers(event.getSubscribers() + " " + id);
        ArrayList <Event>events=page.getEvents();
        events.add(event);
        page.setEvents(events);
        DatePicker.setValue(null);

         */
    }

    public void onBackButtonEnter(MouseEvent mouseEvent){
        BackButton.setStyle("-fx-background-color: #b3b3b3");
    }
    public void onBackButtonExit(MouseEvent mouseEvent){
        BackButton.setStyle("-fx-background-color: CDCDCD");
    }


    private void searchFilter() {
        //---------search filter----------------------
        FilteredList<EventsTable> filteredData =
                new FilteredList<>(modelGrade, b -> true);
        SearchTextField.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(EventsTable -> {
                    if (newValue.isEmpty() || newValue.isBlank() ||
                            newValue == null) {
                        return true;
                    }
                    String searchKeyword = newValue.toLowerCase();
                    if (EventsTable.getC1().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (EventsTable.getC2().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else
                        return false;
                })
        );
        SortedList<EventsTable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }


    public void setServices(Page page) {
        this.serviceAccount=page.getServiceAccount();
        this.serviceEvent=page.getServiceEvent();
        this.page=page;
        //notify the new events
        page.notifyAllObservers();
        SearchTextField.setVisible(false);
        SearchImage.setVisible(false);
        table.setVisible(false);
        gridPane.setVisible(false);
    }
}
