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
    private ServiceUserFriendship serviceUserFriendship;
    private ServiceMessage serviceMessage;
    private ServiceFriendshipRequest serviceFriendshipRequest;
    private ServiceAccount serviceAccount;

    private ArrayList<User>Friends=new ArrayList<User>();
    private ArrayList<FriendshipRequest>friendshipRequests=new ArrayList<FriendshipRequest>();


    public Page(Account account, ServiceUserFriendship serviceUserFriendship,
                ServiceMessage serviceMessage, ServiceFriendshipRequest serviceFriendshipRequest,ServiceAccount serviceAccount) throws RepoException {
        this.account = account;
        this.serviceUserFriendship = serviceUserFriendship;
        this.serviceMessage = serviceMessage;
        this.serviceFriendshipRequest = serviceFriendshipRequest;
        this.serviceAccount=serviceAccount;
        createFriendsList();
        createRequestsLists();
    }

    private void createRequestsLists() throws RepoException {
        for(FriendshipRequest fr:serviceFriendshipRequest.getAllRequests())
            if(fr.getSender().getId()==account.getUser_id()||fr.getReceiver().getId()==account.getUser_id())
                friendshipRequests.add(fr);
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
