package com.example.chatr;

import com.example.chatr.domain.Account;
import com.example.chatr.domain.Friendship;
import com.example.chatr.domain.FriendshipRequest;
import com.example.chatr.domain.User;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.service.ServiceAccount;
import com.example.chatr.service.ServiceFriendshipRequest;
import com.example.chatr.service.ServiceMessage;
import com.example.chatr.service.ServiceUserFriendship;

import java.util.ArrayList;
import java.util.Objects;

public class Page {
    private Account account;
    private ServiceAccount serviceAccount;
    private ServiceUserFriendship serviceUserFriendship;
    private ServiceMessage serviceMessage;
    private ServiceFriendshipRequest serviceFriendshipRequest;

    private ArrayList<User>Friends;
    private ArrayList<User>RequestSentTo;
    private ArrayList<User>RequestReceivedFrom;
    public Page(Account account, ServiceAccount serviceAccount, ServiceUserFriendship serviceUserFriendship,
                ServiceMessage serviceMessage, ServiceFriendshipRequest serviceFriendshipRequest) throws RepoException {
        this.account = account;
        this.serviceAccount = serviceAccount;
        this.serviceUserFriendship = serviceUserFriendship;
        this.serviceMessage = serviceMessage;
        this.serviceFriendshipRequest = serviceFriendshipRequest;
        createFriendsList();
        createRequestsLists();
    }

    private void createRequestsLists() throws RepoException {
        for(FriendshipRequest friendshipRequest:serviceFriendshipRequest.getAllRequests()){
            if(friendshipRequest.getSender().getId()==account.getUser_id())
                RequestSentTo.add(serviceUserFriendship.find_user_by_id(friendshipRequest.getSender().getId()));
            else if(friendshipRequest.getReceiver().getId()==account.getUser_id())
                RequestReceivedFrom.add(serviceUserFriendship.find_user_by_id(friendshipRequest.getReceiver().getId()));
        }
    }

    private void createFriendsList() throws RepoException {
        for(Friendship friendship:serviceUserFriendship.get_all_friendships()){
            if(friendship.getUser1().getId()==account.getUser_id())
                Friends.add(serviceUserFriendship.find_user_by_id(friendship.getUser2().getId()));
            else if(friendship.getUser2().getId()==account.getUser_id())
                Friends.add(serviceUserFriendship.find_user_by_id(friendship.getUser1().getId()));
        }
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public ServiceAccount getServiceAccount() {
        return serviceAccount;
    }

    public void setServiceAccount(ServiceAccount serviceAccount) {
        this.serviceAccount = serviceAccount;
    }

    public ServiceUserFriendship getServiceUserFriendship() {
        return serviceUserFriendship;
    }

    public void setServiceUserFriendship(ServiceUserFriendship serviceUserFriendship) {
        this.serviceUserFriendship = serviceUserFriendship;
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

    public ArrayList<User> getFriends() {
        return Friends;
    }

    public void setFriends(ArrayList<User> friends) {
        Friends = friends;
    }

    public ArrayList<User> getRequestSentTo() {
        return RequestSentTo;
    }

    public void setRequestSentTo(ArrayList<User> requestSentTo) {
        RequestSentTo = requestSentTo;
    }

    public ArrayList<User> getRequestReceivedFrom() {
        return RequestReceivedFrom;
    }

    public void setRequestReceivedFrom(ArrayList<User> requestReceivedFrom) {
        RequestReceivedFrom = requestReceivedFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Page)) return false;
        Page page = (Page) o;
        return account.equals(page.account) && serviceAccount.equals(page.serviceAccount) && serviceUserFriendship.equals(page.serviceUserFriendship) &&
                serviceMessage.equals(page.serviceMessage) && serviceFriendshipRequest.equals(page.serviceFriendshipRequest) && Friends.equals(page.Friends)
                && RequestSentTo.equals(page.RequestSentTo) && RequestReceivedFrom.equals(page.RequestReceivedFrom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, serviceAccount, serviceUserFriendship, serviceMessage, serviceFriendshipRequest, Friends, RequestSentTo, RequestReceivedFrom);
    }

    @Override
    public String toString() {
        return "Page{" +
                "Friends=" + Friends +
                ", RequestSentTo=" + RequestSentTo +
                ", RequestReceivedFrom=" + RequestReceivedFrom +
                '}';
    }
}
