package service;

import domain.Message;
import domain.User;
import exceptions.MessageException;
import repository.Repo;
import validators.MessageValidator;
import validators.StrategyValidator;

import java.util.ArrayList;
import java.util.List;

public class ServiceMessage {
    private final Repo<Integer, User> userRepo;
    private final Repo<Integer, Message> messageRepo;
    private final StrategyValidator<Message> messageValidator;

    public ServiceMessage(Repo<Integer, User> userRepo, Repo<Integer, Message> messageRepo,
                          StrategyValidator<Message> messageValidator) {
        this.userRepo = userRepo;
        this.messageRepo = messageRepo;
        this.messageValidator = messageValidator;
    }

    public ServiceMessage(Repo<Integer, User> userRepo, Repo<Integer, Message> messageRepo) {
        this.userRepo = userRepo;
        this.messageRepo = messageRepo;
        this.messageValidator = MessageValidator.getInstance();
    }

    public List<Integer> getIdsFromString(String input){
        String[] idsArray = input.split(",");
        List<Integer> toIds = new ArrayList<>();
        for (String id: idsArray){
            toIds.add(Integer.parseInt(id));
        }
        return toIds;
    }

    public void sendMessage(int fromId, List<Integer> toIds, String message) throws Exception {
        User from = userRepo.find_by_id(fromId);
        List<User> toUsers = new ArrayList<>();
        for(Integer id: toIds){
            User user = userRepo.find_by_id(id);
            toUsers.add(user);
        }
        Message messageToSend = new Message(from,toUsers,message);
        messageRepo.add(messageToSend);
    }
}
