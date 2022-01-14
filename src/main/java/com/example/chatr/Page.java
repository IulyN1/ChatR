package com.example.chatr;

import com.example.chatr.domain.*;
import com.example.chatr.exceptions.FriendshipRequestException;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.service.*;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Objects;

public class Page {
    private Account account;
    private ServiceUserFriendship serviceUserFriendship;
    private ServiceMessage serviceMessage;
    private ServiceFriendshipRequest serviceFriendshipRequest;
    private ServiceAccount serviceAccount;
    private ServiceEvent serviceEvent;

    private boolean enter;

    private ArrayList<User>Friends=new ArrayList<User>();
    private ArrayList<FriendshipRequest>friendshipRequests=new ArrayList<FriendshipRequest>();
    private ArrayList<Event>events=new ArrayList<Event>();

    public Page(Account account, ServiceUserFriendship serviceUserFriendship,
                ServiceMessage serviceMessage, ServiceFriendshipRequest serviceFriendshipRequest,ServiceAccount serviceAccount,ServiceEvent serviceEvent) throws RepoException {
        this.account = account;
        this.serviceUserFriendship = serviceUserFriendship;
        this.serviceMessage = serviceMessage;
        this.serviceFriendshipRequest = serviceFriendshipRequest;
        this.serviceAccount=serviceAccount;
        this.serviceEvent=serviceEvent;
        createFriendsList();
        createRequestsLists();
        createEvenetsList();
        enter=true;
    }

    public void refresh() throws RepoException {
        Friends.clear();
        friendshipRequests.clear();
        events.clear();
        createFriendsList();
        createRequestsLists();
        createEvenetsList();
    }

    private void createRequestsLists() throws RepoException {
        for(FriendshipRequest fr:serviceFriendshipRequest.getAllRequests())
            if(fr.getSender().getId()==account.getUser_id()||fr.getReceiver().getId()==account.getUser_id())
                friendshipRequests.add(fr);
    }

    private void createFriendsList() throws RepoException {
        for(Friendship friendship:serviceUserFriendship.getAllFriendships()){
            if(friendship.getUser1().getId()==account.getUser_id())
                Friends.add(serviceUserFriendship.findUserById(friendship.getUser2().getId()));
            else if(friendship.getUser2().getId()==account.getUser_id())
                Friends.add(serviceUserFriendship.findUserById(friendship.getUser1().getId()));
        }
    }

    private void createEvenetsList(){
        for(Event event:serviceEvent.getAllEvent()) {
            if (event.getSubscribers().contains(" ")) {
                String[] splited = event.getSubscribers().split(" ");
                for (String str : splited) {
                    if(!str.equals(""))
                    if (account.getUser_id() == Integer.parseInt(str)) {
                        events.add(event);
                        break;
                    }
                }
            }
        }
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    public ServiceUserFriendship getServiceUserFriendship() {
        return serviceUserFriendship;
    }

    public void setServiceUserFriendship(ServiceUserFriendship serviceUserFriendship) {
        this.serviceUserFriendship = serviceUserFriendship;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public ServiceMessage getServiceMessage() {
        return serviceMessage;
    }

    public void setServiceMessage(ServiceMessage serviceMessage) {
        this.serviceMessage = serviceMessage;
    }

    public ServiceFriendshipRequest getServiceFriendshipRequest() {
        return serviceFriendshipRequest;
    }

    public void setServiceFriendshipRequest(ServiceFriendshipRequest serviceFriendshipRequest) {
        this.serviceFriendshipRequest = serviceFriendshipRequest;
    }

    public ServiceEvent getServiceEvent() {
        return serviceEvent;
    }

    public void setServiceEvent(ServiceEvent serviceEvent) {
        this.serviceEvent = serviceEvent;
    }

    public ArrayList<User> getFriends() {
        return Friends;
    }

    public void setFriends(ArrayList<User> friends) {
        Friends = friends;
    }

    public ServiceAccount getServiceAccount() {
        return serviceAccount;
    }

    public void setServiceAccount(ServiceAccount serviceAccount) {
        this.serviceAccount = serviceAccount;
    }


    public ArrayList<FriendshipRequest> getFriendshipRequests() {
        return friendshipRequests;
    }

    public void setFriendshipRequests(ArrayList<FriendshipRequest> friendshipRequests) {
        this.friendshipRequests = friendshipRequests;
    }

    public boolean isEnter() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }

    public void addFriends(int receiver_id){
        try {
            checkRequest(receiver_id);
            serviceFriendshipRequest.addFriendshipRequest(account.getUser_id(), receiver_id);
            //update page
            FriendshipRequest friendshipRequest=new FriendshipRequest(serviceUserFriendship.findUserById(account.getUser_id()),serviceUserFriendship.findUserById(receiver_id));
            ArrayList<FriendshipRequest>friends=getFriendshipRequests();
            friends.add(friendshipRequest);
            setFriendshipRequests(friends);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setContentText("Friendship request sent!");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.setContentText("Press Ok to go back!");
            alert.showAndWait();
        }
    }

    public void deleteRequest(int receiver_id){
        try {
            for (FriendshipRequest fr : serviceFriendshipRequest.getAllRequests()) {
                if (fr.getSender().getId() == account.getUser_id() && fr.getReceiver().getId() == receiver_id)
                    serviceFriendshipRequest.deleteFriendshipRequest(fr.getId());
            }
            //update page
            ArrayList<FriendshipRequest> friends =getFriendshipRequests();
            FriendshipRequest fr=new FriendshipRequest(serviceUserFriendship.findUserById(account.getUser_id()),
                    serviceUserFriendship.findUserById(receiver_id));
            friends.remove(fr);
            setFriendshipRequests(friends);
            //alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setContentText("The friend request has been withdrawn!");
            alert.showAndWait();
        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.setContentText("Press Ok to go back!");
            alert.showAndWait();
        }
    }

    public void respondRequest(String status,int id) throws RepoException {
        try {
            for (FriendshipRequest fr : getFriendshipRequests()) {
                if (fr.getSender().getId() == id ) {
                    //update Page
                    ArrayList<FriendshipRequest>pageRequests=getFriendshipRequests();
                    pageRequests.remove(fr);

                    fr.setStatus(status);
                    serviceFriendshipRequest.updateFriendshipRequest(fr);

                    pageRequests.add(fr);

                    if (status.equals("APPROVED")) {
                        serviceUserFriendship.addFriendship(fr.getSender().getId(), fr.getReceiver().getId());
                        //update page
                        ArrayList<User>friends=getFriends();
                        friends.add(serviceUserFriendship.findUserById(id));
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success!");
                    if (status.equals("REJECTED"))
                        alert.setContentText("Request rejected!");
                    if (status.equals("APPROVED"))
                        alert.setContentText("Request approved!");
                    alert.showAndWait();
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

    public void deleteFriend(int otherId) {
        try{
            User currentUser = serviceUserFriendship.findUserById(account.getUser_id());
            int currentId = currentUser.getId();

            // delete the friendship
            for(Friendship fr: serviceUserFriendship.getAllFriendships()){
                if(fr.getUser1().getId().equals(currentId) && fr.getUser2().getId().equals(otherId)){
                    serviceUserFriendship.deleteFriendship(fr.getId());
                }
                else if(fr.getUser2().getId().equals(currentId) && fr.getUser1().getId().equals(otherId)){
                    serviceUserFriendship.deleteFriendship(fr.getId());
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
            //delete from page
            ArrayList<User>friends=getFriends();
            friends.remove(serviceUserFriendship.findUserById(otherId));
            setFriends(friends);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success!");
            alert.setContentText("Press OK to go back!");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.setContentText("Press OK to go back!");
            alert.showAndWait();
        }
    }

    public void notifyAllObservers(){
        for(Event observer:events){
            observer.update();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Page)) return false;
        Page page = (Page) o;
        return account.equals(page.account) && serviceUserFriendship.equals(page.serviceUserFriendship) && serviceMessage.equals(page.serviceMessage) && serviceFriendshipRequest.equals(page.serviceFriendshipRequest) && serviceAccount.equals(page.serviceAccount) && Friends.equals(page.Friends) && friendshipRequests.equals(page.friendshipRequests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, serviceUserFriendship, serviceMessage, serviceFriendshipRequest, serviceAccount, Friends, friendshipRequests);
    }

    @Override
    public String toString() {
        return "Page{" +
                "account=" + account +
                ", Friends=" + Friends +
                ", friendshipRequests=" + friendshipRequests +
                '}';
    }
}
