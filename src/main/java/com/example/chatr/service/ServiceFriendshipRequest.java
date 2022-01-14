package com.example.chatr.service;


import com.example.chatr.domain.FriendshipRequest;
import com.example.chatr.domain.User;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.repository.Repo;
import com.example.chatr.validators.StrategyValidator;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;


public class ServiceFriendshipRequest {
    private final Repo<Integer, User> userRepo;
    private final Repo<Integer, FriendshipRequest> friendshipRequestRepo;
    private final StrategyValidator<FriendshipRequest> friendshipRequestValidator;

    /**
     * Constructor for FriendshipRequest
     *
     * @param userRepo
     * @param friendshipRequestRepo
     * @param friendshipRequestValidator
     */
    public ServiceFriendshipRequest(Repo<Integer, User> userRepo, Repo<Integer,
            FriendshipRequest> friendshipRequestRepo, StrategyValidator<FriendshipRequest> friendshipRequestValidator) {
        this.userRepo = userRepo;
        this.friendshipRequestRepo = friendshipRequestRepo;
        this.friendshipRequestValidator = friendshipRequestValidator;
    }

    /**
     * add a friendship request between 2 users
     *
     * @param id1
     * @param id2
     * @throws Exception
     */
    public void addFriendshipRequest(int id1, int id2) throws Exception {
        User sender = userRepo.findById(id1);
        User receiver = userRepo.findById(id2);
        System.out.println(sender);
        System.out.println(receiver);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = sdf.format(new Date());
        FriendshipRequest friendshipReq = new FriendshipRequest(sender, receiver, "PENDING", date);
        friendshipRequestValidator.validate(friendshipReq);
        friendshipRequestRepo.add(friendshipReq);
    }

    /**
     * update a FriendshipRequest between 2 users
     *
     * @throws Exception
     */
    public void updateFriendshipRequest(FriendshipRequest friendshipReq) throws Exception {
        friendshipRequestValidator.validate(friendshipReq);
        friendshipRequestRepo.update(friendshipReq);
    }


    /**
     * delete a friendshipReq between 2 users
     *
     * @param id
     * @return
     * @throws RepoException
     */
    public FriendshipRequest deleteFriendshipRequest(int id) throws RepoException {
        return friendshipRequestRepo.delete(id);
    }

    /**
     * find a friendship request by  its id
     *
     * @param id
     * @return
     * @throws RepoException
     */
    public FriendshipRequest findFriendshipRequestById(int id) throws RepoException {
        return friendshipRequestRepo.findById(id);
    }

    /**
     * Reply for a friendshipRequest***you can reply just if the req status is PENDING!
     *
     * @param id     of request
     * @param status
     * @throws Exception
     */
    public void friendshipReplyRequest(int id, String status) throws Exception {
        FriendshipRequest friendshipRequest = friendshipRequestRepo.findById(id);
        if (friendshipRequest.getStatus() == "APPROVED")
            System.out.println("Friendship is already approved");
        else if (friendshipRequest.getStatus() == "REJECTED")
            System.out.println("Friendship is already rejected");
        else {
            friendshipRequest.setStatus(status);
            //updateFriendshipRequest(id,friendshipRequest.getSender().getId(),
            //      friendshipRequest.getReceiver().getId(),status);
            if (status == "APPROVED") {

            }
        }
    }

    /**
     * @return a collection of all FriendshipRequest
     */
    public Collection<FriendshipRequest> getAllRequests() {
        return friendshipRequestRepo.findAll();
    }

}
