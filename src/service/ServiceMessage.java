package service;

import domain.Message;
import domain.User;
import exceptions.MessageException;
import repository.Repo;
import validators.MessageValidator;
import validators.StrategyValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ServiceMessage {
    private final Repo<Integer, User> userRepo;
    private final Repo<Integer, Message> messageRepo;
    private final StrategyValidator<Message> messageValidator;

    /**
     * Constructor for ServiceMessage
     * @param userRepo the users repo
     * @param messageRepo the messages repo
     * @param messageValidator the validator for messages
     */
    public ServiceMessage(Repo<Integer, User> userRepo, Repo<Integer, Message> messageRepo,
                          StrategyValidator<Message> messageValidator) {
        this.userRepo = userRepo;
        this.messageRepo = messageRepo;
        this.messageValidator = messageValidator;
    }

    /**
     * Constructor for ServiceMessage
     * @param userRepo the users repo
     * @param messageRepo the messages repo
     */
    public ServiceMessage(Repo<Integer, User> userRepo, Repo<Integer, Message> messageRepo) {
        this.userRepo = userRepo;
        this.messageRepo = messageRepo;
        this.messageValidator = MessageValidator.getInstance();
    }

    /**
     * Gets a list of integer ids from a string input
     * @param input the ids split by ,
     * @return the list of ids
     */
    public List<Integer> getIdsFromString(String input){
        String[] idsArray = input.split(",");
        List<Integer> toIds = new ArrayList<>();
        for (String id: idsArray){
            toIds.add(Integer.parseInt(id));
        }
        return toIds;
    }

    /**
     * Sends a message
     * @param fromId the id of the user who sends the message
     * @param toIds the ids of the users to whom the message is sent
     * @param message the message itself
     * @throws Exception if the operation fails
     */
    public void sendMessage(int fromId, List<Integer> toIds, String message) throws Exception {
        User from = userRepo.find_by_id(fromId);
        List<User> toUsers = new ArrayList<>();
        for(Integer id: toIds){
            User user = userRepo.find_by_id(id);
            toUsers.add(user);
        }
        Message messageToSend = new Message(from,toUsers,message);
        messageValidator.validate(messageToSend);
        messageRepo.add(messageToSend);
    }

    /**
     * Gets all the messages
     * @return a collection of all the messages
     */
    public Collection<Message> get_all_messages(){
        return messageRepo.find_all();
    }
}
