package com.example.chatr.controllers;

import com.example.chatr.Application;
import com.example.chatr.Page;
import com.example.chatr.domain.*;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.service.ServiceMessage;
import com.example.chatr.service.ServiceUserFriendship;
import com.example.chatr.utils.Constants;
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
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
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
        if(dateStart != null && dateEnd != null && dateStart.compareTo(dateEnd)<=0) {
            List<Friendship> friendships = serviceUserFriendship.getAllFriendships().stream().toList();
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

            List<Message> messages = serviceMessage.getAllMessages().stream().toList();
            List<Message> messagesReport = new ArrayList<>();
            User thisUser = serviceUserFriendship.findUserById(currentAccount.getUser_id());
            for (Message msg : messages) {
                if (msg.getTo().contains(thisUser)) {
                    LocalDateTime dateMsg = msg.getDate();
                    LocalDate date = dateMsg.toLocalDate();
                    if (date.compareTo(dateStart) >= 0 && date.compareTo(dateEnd) <= 0) {
                        messagesReport.add(msg);
                    }
                }
            }

            FileChooser fileChooser = new FileChooser();
            //Set extension filter for pdf files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);
            //Show save file dialog
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);
                try {
                    PDPageContentStream content = new PDPageContentStream(document, document.getPage(0),
                            PDPageContentStream.AppendMode.APPEND,true,true);
                    content.beginText();
                    content.setFont(PDType1Font.COURIER_BOLD, 20);
                    content.newLineAtOffset(10, 740);
                    content.showText("Friends");
                    content.newLineAtOffset(200, 0);
                    content.showText("Date");
                    content.setFont(PDType1Font.COURIER, 15);
                    for(Friendship fr: friendshipsReport){
                        content.newLineAtOffset(-200, -20);
                        if(fr.getUser1().getId().equals(currentAccount.getUser_id())){
                            content.showText(fr.getUser2().getFirstName() + " " + fr.getUser2().getLastName());
                        }
                        else{
                            content.showText(fr.getUser1().getFirstName() + " " + fr.getUser1().getLastName());
                        }
                        content.newLineAtOffset(200,0);
                        content.showText(fr.getFriendshipDate());
                    }
                    content.setFont(PDType1Font.COURIER_BOLD, 20);
                    content.newLineAtOffset(-200, -40);
                    content.showText("Messages");
                    content.newLineAtOffset(200, 0);
                    content.showText("From");
                    content.newLineAtOffset(200, 0);
                    content.showText("Date");
                    content.setFont(PDType1Font.COURIER, 15);
                    for(Message msg: messagesReport){
                        content.newLineAtOffset(-400, -20);
                        content.showText(msg.getMessage());
                        content.newLineAtOffset(200, 0);
                        content.showText(msg.getFrom().getFirstName() + " " + msg.getFrom().getLastName());
                        content.newLineAtOffset(200, 0);
                        content.showText(msg.getDate().format(Constants.DATE_TIME_FORMATTER));
                    }
                    content.endText();
                    content.close();
                    document.save(file);
                    document.close();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Report exported");
                    alert.setHeaderText("Report successfully created and exported!");
                    alert.setContentText("Press Ok to go back!");
                    alert.showAndWait();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(e.getMessage());
                    alert.setContentText("Press Ok to go back!");
                    alert.showAndWait();
                }
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please input valid dates!");
            alert.setContentText("Press Ok to go back!");
            alert.showAndWait();
        }
    }

    public void onGenerateButton2Clicked(MouseEvent mouseEvent) throws Exception {
        LocalDate dateStart = dateStart2.getValue();
        LocalDate dateEnd = dateEnd2.getValue();
        UserDTO sender = userList.getValue();
        if (dateStart != null && dateEnd != null && sender != null && dateStart.compareTo(dateEnd)<=0) {

            List<Message> messages = serviceMessage.getChat(currentAccount.getUser_id(), sender.getId()).stream().toList();
            List<Message> messagesReport = new ArrayList<>();
            User thisUser = serviceUserFriendship.findUserById(currentAccount.getUser_id());
            for (Message msg : messages) {
                if (msg.getTo().contains(thisUser)) {
                    LocalDateTime dateMsg = msg.getDate();
                    LocalDate date = dateMsg.toLocalDate();
                    if (date.compareTo(dateStart) >= 0 && date.compareTo(dateEnd) <= 0) {
                        messagesReport.add(msg);
                    }
                }
            }

            FileChooser fileChooser = new FileChooser();
            //Set extension filter for pdf files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);
            //Show save file dialog
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);
                try {
                    PDPageContentStream content = new PDPageContentStream(document, document.getPage(0),
                            PDPageContentStream.AppendMode.APPEND, true, true);
                    content.beginText();
                    content.setFont(PDType1Font.COURIER_BOLD, 20);
                    content.newLineAtOffset(10, 740);
                    content.showText("Messages from: " + userList.getValue());
                    content.newLineAtOffset(0, -40);
                    content.showText("Messages");
                    content.newLineAtOffset(200, 0);
                    content.showText("Date");
                    content.setFont(PDType1Font.COURIER, 15);
                    for (Message msg : messagesReport) {
                        content.newLineAtOffset(-200, -20);
                        content.showText(msg.getMessage());
                        content.newLineAtOffset(200, 0);
                        content.showText(msg.getDate().format(Constants.DATE_TIME_FORMATTER));
                    }
                    content.endText();
                    content.close();
                    document.save(file);
                    document.close();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Report exported");
                    alert.setHeaderText("Report successfully created and exported!");
                    alert.setContentText("Press Ok to go back!");
                    alert.showAndWait();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(e.getMessage());
                    alert.setContentText("Press Ok to go back!");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please input valid dates and user!");
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
        Image img = new Image("logo.png");
        stage.getIcons().add(img);
        stage.show();
    }
}
