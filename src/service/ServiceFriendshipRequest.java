package service;

import domain.Friendship;

import domain.FriendshipRequest;
import domain.User;
import exceptions.RepoException;
import repository.Repo;
import validators.StrategyValidator;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class ServiceFriendshipRequest {
    private final Repo<Integer, User> userRepo;
    private final Repo<Integer, FriendshipRequest> friendshipRequestRepo;
    private final StrategyValidator<User> userValidator;

    public ServiceFriendshipRequest(Repo<Integer, User> userRepo, Repo<Integer,
            FriendshipRequest> friendshipRequestRepo, StrategyValidator<User> userValidator) {
        this.userRepo = userRepo;
        this.friendshipRequestRepo = friendshipRequestRepo;
        this.userValidator = userValidator;
    }

    public void addFriendshipRequest(int id1,int id2) throws Exception{
        User sender = userRepo.find_by_id(id1);
        User receiver = userRepo.find_by_id(id2);
        FriendshipRequest friendshipReq = new FriendshipRequest(sender, receiver);
        //add vailidator
        friendshipRequestRepo.add(friendshipReq);
    }

    public void updateFriendshipRequest(int id, int idUser1, int idUser2,String status) throws Exception{
        User sender = userRepo.find_by_id(idUser1);
        User receiver= userRepo.find_by_id(idUser2);
        FriendshipRequest friendshipReq = new FriendshipRequest(sender, receiver,status);
        //add vailidator
        friendshipRequestRepo.update(friendshipReq);
    }



    public FriendshipRequest deleteFriendshipRequest(int id) throws RepoException{
        return friendshipRequestRepo.delete(id);
    }

    public FriendshipRequest findFriendshipRequestById(int id) throws RepoException{
        return friendshipRequestRepo.find_by_id(id);
    }

    public void friendshipReplyRequest(int id,String status) throws Exception{
        FriendshipRequest friendshipRequest=friendshipRequestRepo.find_by_id(id);
        if(friendshipRequest.getStatus()=="APPROVED")
            System.out.println("Friendship is already approved");
        else
            if(friendshipRequest.getStatus()=="REJECTED")
                System.out.println("Friendship is already rejected");
            else{
                friendshipRequest.setStatus(status);
                friendshipRequestRepo.update(friendshipRequest);
                if(status=="APPROVED"){

                }
            }
    }

    public Collection<FriendshipRequest> getAllRequests(){
        return friendshipRequestRepo.find_all();
    }

}
