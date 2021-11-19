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
    //*add friendshipvalidator


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

    public void updateFriendshipRequest(int id, int idUser1, int idUser2) throws Exception{
        User sender = userRepo.find_by_id(idUser1);
        User receiver= userRepo.find_by_id(idUser2);
        Friendship friendship = new Friendship(id,sender,receiver);
        FriendshipRequest friendshipReq = new FriendshipRequest(sender, receiver);
        //add vailidator
        friendshipRequestRepo.add(friendshipReq);
    }



    public FriendshipRequest deleteFriendshipRequest(int id) throws RepoException{
        return friendshipRequestRepo.delete(id);
    }

    public FriendshipRequest findFriendshipRequestById(int id) throws RepoException{
        return friendshipRequestRepo.find_by_id(id);
    }
    public Collection<FriendshipRequest> getAllRequests(){
        return friendshipRequestRepo.find_all();
    }

}
