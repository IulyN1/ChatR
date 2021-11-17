package service;

import domain.Message;
import domain.User;
import exceptions.MessageException;
import repository.Repo;
import validators.MessageValidator;
import validators.StrategyValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

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
     * Sends a reply message
     * @param idMessage the id of the message to which the user replies
     * @param idReplier the id of the user who replies
     * @param message the message itself
     * @throws Exception if the operation fails
     */
    public void sendReplyMessage(int idMessage,int idReplier,String message) throws Exception {
        Message messageToReplyTo = messageRepo.find_by_id(idMessage);
        List<User> to = messageToReplyTo.getTo();
        User replier = userRepo.find_by_id(idReplier);

        if(to.contains(replier)){
            List<User> receiver = new ArrayList<>();
            receiver.add(messageToReplyTo.getFrom());
            Message messageToSend = new Message(replier,receiver,message);
            messageValidator.validate(messageToSend);
            messageToSend.setReply(messageToReplyTo);
            messageRepo.add(messageToSend);
        }
        else throw new MessageException("User didn't receive that message!\n");
    }

    /**
     * Gets all the messages
     * @return a collection of all the messages
     */
    public Collection<Message> get_all_messages(){
        return messageRepo.find_all();
    }

    /**
     * Gets all the messages between 2 users in chronological order
     * @param idUser1 the id of the first user
     * @param idUser2 the id of the second user
     * @return a collection of messages
     */
    public Collection<Message> get_chat(int idUser1,int idUser2) throws Exception{
        Collection<Message> messages = messageRepo.find_all();
        Stream<Message> stream =  messages.stream().
                sorted(Comparator.comparing(Message::getDate));
        List<Message> messagesSorted = stream.toList();
        Collection<Message> chat = new ArrayList<>();

        for(Message msg: messagesSorted){
            if(msg.getFrom().getId()==idUser1){
                for(User us: msg.getTo()){
                    if(us.equals(userRepo.find_by_id(idUser2)))
                        chat.add(msg);
                }
            }
            else if(msg.getFrom().getId()==idUser2){
                for(User us: msg.getTo()){
                    if(us.equals(userRepo.find_by_id(idUser1)))
                        chat.add(msg);
                }
            }
        }
        if(chat.isEmpty()) throw new MessageException("There are no messages between these users!\n");
        return chat;
    }
}
