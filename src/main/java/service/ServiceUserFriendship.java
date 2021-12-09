package service;

import domain.Friendship;
import domain.User;
import exceptions.RepoException;
import repository.Repo;
import utils.Network;
import utils.Pair;
import validators.FriendshipValidator;
import validators.PairValidator;
import validators.StrategyValidator;
import validators.UserValidator;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ServiceUserFriendship {
    private final Repo<Integer, User> userRepo;
    private final Repo<Integer, Friendship> friendshipRepo;
    private final StrategyValidator<User> userValidator;
    private final StrategyValidator<Friendship> friendshipValidator;

    /**
     * Constructor for Service
     * @param userRepo repository used for Users
     * @param friendshipRepo repository used for Friendships
     * @throws Exception if an operation fails
     */
    public ServiceUserFriendship(Repo<Integer, User> userRepo, Repo<Integer, Friendship> friendshipRepo) throws  Exception{
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
        this.userValidator = UserValidator.getInstance();
        this.friendshipValidator = FriendshipValidator.getInstance();
        PairValidator.getInstance().validate(new Pair<>(userRepo,friendshipRepo));
        bind_friendships();
    }

    /**
     * Constructor for Service
     * @param userRepo repository used for Users
     * @param friendshipRepo repository used for Friendships
     * @param userValidator validator used for validating an user
     * @param friendshipValidator validator used for validating a friendship
     * @throws Exception if an operation fails
     */
    public ServiceUserFriendship(Repo<Integer, User> userRepo, Repo<Integer, Friendship> friendshipRepo,
                                 UserValidator userValidator, FriendshipValidator friendshipValidator) throws  Exception{
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
        this.userValidator = userValidator;
        this.friendshipValidator = friendshipValidator;
        PairValidator.getInstance().validate(new Pair<>(userRepo,friendshipRepo));
        bind_friendships();
    }

    /**
     * Creates the friendships, binds the users to a friendship
     */
    private void bind_friendships(){
        for(User user: userRepo.find_all()){
            for(Friendship friendship: friendshipRepo.find_all()){
                if(user.equals(friendship.getUser1())){
                    friendship.setUser1(user);
                }
                else if(user.equals(friendship.getUser2())){
                    friendship.setUser2(user);
                }
            }
        }
    }

    /**
     * Adds an user based on first and last name
     * @param firstName first name of the new user
     * @param lastName last name of the new user
     * @throws Exception if an operation fails
     */
    public void add_user(String firstName, String lastName) throws  Exception{
        User user = new User(firstName,lastName);
        userValidator.validate(user);
        userRepo.add(user);
    }

    /**
     * Deletes an user based on it's ID
     * @param id the ID of the specific user
     * @return the deleted user
     * @throws RepoException if the user doesn't exist
     */
    public User delete_user(int id) throws RepoException{
        User user = userRepo.delete(id);

        Collection<Friendship> collection = friendshipRepo.find_all();
        for(Friendship friendship: collection.stream().toList()){
            if(friendship.getUser1().equals(user))
                friendshipRepo.delete(friendship.getId());
        }
        return user;
    }

    /**
     * Modifies an user after it's ID
     * @param id the ID of the specific user
     * @param firstName the new first name of the user
     * @param lastName the new last name of the user
     * @throws Exception if the operation fails
     */
    public void update_user(int id, String firstName, String lastName) throws Exception{
        User user = new User(id,firstName,lastName);
        User old_user = find_user_by_id(id);
        userValidator.validate(user);
        userRepo.update(user);

        Collection<Friendship> collection = friendshipRepo.find_all();
        for(Friendship friendship: collection.stream().toList()){
            if(friendship.getUser1().equals(old_user)) {
                friendship.setUser1(user);
                friendshipRepo.update(friendship);
            }
            if(friendship.getUser2().equals(old_user)) {
                friendship.setUser2(user);
                friendshipRepo.update(friendship);
            }
        }
    }

    /**
     * Searches the user by ID
     * @param id the ID of the searched user
     * @return the user if it was found
     * @throws RepoException if the user doesn't exist
     */
    public User find_user_by_id(int id) throws RepoException{
        return userRepo.find_by_id(id);
    }

    /**
     * Adds a friendship between 2 users
     * @param id1 the ID of the first user
     * @param id2 the ID of the second user
     * @throws Exception if the operation fails
     */
    public void add_friendship(int id1,int id2) throws Exception{
        User user1 = userRepo.find_by_id(id1);
        User user2 = userRepo.find_by_id(id2);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = sdf.format(new Date());
        Friendship friendship = new Friendship(user1, user2,date);
        friendshipValidator.validate(friendship);
        friendshipRepo.add(friendship);
    }
    /**
     * Updates a friendship
     * @param id the id of the friendship
     * @param idUser1 the id of the first user
     * @param idUser2 the id of the second user
     * @throws Exception if the operation fails
     */
    public void update_friendship(int id, int idUser1, int idUser2) throws Exception{
        User user1 = userRepo.find_by_id(idUser1);
        User user2 = userRepo.find_by_id(idUser2);
        Friendship friendship = new Friendship(id,user1,user2);
        friendshipValidator.validate(friendship);
        friendshipRepo.update(friendship);
    }


    /**
     * Deletes a friendship after the ID
     * @param id the ID of the specific friendship
     * @return the deleted friendship
     * @throws RepoException if the friendship doesn't exist
     */
    public Friendship delete_friendship(int id) throws RepoException{
        return friendshipRepo.delete(id);
    }

    /**
     * Searches friendship by ID
     * @param id the ID of the friendship searched
     * @return the found friendship
     * @throws RepoException if the friendship doesn't exist
     */
    public Friendship find_friendship_by_id(int id) throws RepoException{
        return friendshipRepo.find_by_id(id);
    }

    /**
     * Return all the users
     * @return an iterable collection with all the users
     */
    public Collection<User> get_all_users(){
        return userRepo.find_all();
    }

    /**
     * Return all the friendships
     * @return an iterable collection with all the friendships
     */
    public Collection<Friendship> get_all_friendships(){
        return friendshipRepo.find_all();
    }

    /**
     * Gets the number of related friendship communities
     * @return Integer representing this number
     */
    public int nr_related_friendships(){
        List<User> users = userRepo.find_all().stream().toList();
        List<Friendship> friendships = friendshipRepo.find_all().stream().toList();
        Network network = new Network(users,friendships);

        return network.nr_connected_components();
    }

    /**
     * Gets the longest related friendship community
     * @return a collection of iterable String
     */
    public Collection<String> longest_related_friendship(){
        List<User> users = userRepo.find_all().stream().toList();
        List<Friendship> friendships = friendshipRepo.find_all().stream().toList();
        Network network = new Network(users,friendships);

        return network.longest_path();
    }
}
