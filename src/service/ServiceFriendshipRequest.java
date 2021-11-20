package service;


import domain.FriendshipRequest;
import domain.User;
import exceptions.RepoException;
import repository.Repo;
import validators.StrategyValidator;
import java.util.Collection;


public class ServiceFriendshipRequest {
    private final Repo<Integer, User> userRepo;
    private final Repo<Integer, FriendshipRequest> friendshipRequestRepo;
    private final StrategyValidator<FriendshipRequest> friendshipRequestValidator;

    /**
     * Constructor for FriendshipRequest
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
     * @param id1
     * @param id2
     * @throws Exception
     */
    public void addFriendshipRequest(int id1,int id2) throws Exception{
        User sender = userRepo.find_by_id(id1);
        User receiver = userRepo.find_by_id(id2);
        FriendshipRequest friendshipReq = new FriendshipRequest(sender, receiver);
        friendshipRequestValidator.validate(friendshipReq);
        friendshipRequestRepo.add(friendshipReq);
    }

    /**
     * update a FriendshipRequest between 2 users
     * @param id
     * @param idUser1
     * @param idUser2
     * @param status
     * @throws Exception
     */
    public void updateFriendshipRequest(int id, int idUser1, int idUser2,String status) throws Exception{
        User sender = userRepo.find_by_id(idUser1);
        User receiver= userRepo.find_by_id(idUser2);
        FriendshipRequest friendshipReq = new FriendshipRequest(sender, receiver,status);
        friendshipRequestValidator.validate(friendshipReq);
        friendshipRequestRepo.update(friendshipReq);
    }


    /**
     * delete a friendshipReq between 2 users
     * @param id
     * @return
     * @throws RepoException
     */
    public FriendshipRequest deleteFriendshipRequest(int id) throws RepoException{
        return friendshipRequestRepo.delete(id);
    }

    /**
     * find a friendship request by  its id
     * @param id
     * @return
     * @throws RepoException
     */
    public FriendshipRequest findFriendshipRequestById(int id) throws RepoException{
        return friendshipRequestRepo.find_by_id(id);
    }

    /**
     * Reply for a friendshipRequest***you can reply just if the req status is PENDING!
     * @param id of request
     * @param status
     * @throws Exception
     */
    public void friendshipReplyRequest(int id,String status) throws Exception{
        FriendshipRequest friendshipRequest=friendshipRequestRepo.find_by_id(id);
        if(friendshipRequest.getStatus()=="APPROVED")
            System.out.println("Friendship is already approved");
        else
            if(friendshipRequest.getStatus()=="REJECTED")
                System.out.println("Friendship is already rejected");
            else{
                friendshipRequest.setStatus(status);
                updateFriendshipRequest(id,friendshipRequest.getSender().getId(),
                        friendshipRequest.getReceiver().getId(),status);
                if(status=="APPROVED"){

                }
            }
    }

    /**
     *
     * @return a collection of all FriendshipRequest
     */
    public Collection<FriendshipRequest> getAllRequests(){
        return friendshipRequestRepo.find_all();
    }

}
