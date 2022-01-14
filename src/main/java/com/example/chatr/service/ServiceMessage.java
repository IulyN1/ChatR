package com.example.chatr.service;

import com.example.chatr.domain.Message;
import com.example.chatr.domain.User;
import com.example.chatr.exceptions.MessageException;
import com.example.chatr.repository.Repo;
import com.example.chatr.validators.MessageValidator;
import com.example.chatr.validators.StrategyValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class ServiceMessage {
    private final Repo<Integer, User> userRepo;
    private final Repo<Integer, Message> messageRepo;
    private final StrategyValidator<Message> messageValidator;

    /**
     * Constructor for ServiceMessage
     *
     * @param userRepo         the users repo
     * @param messageRepo      the messages repo
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
     *
     * @param userRepo    the users repo
     * @param messageRepo the messages repo
     */
    public ServiceMessage(Repo<Integer, User> userRepo, Repo<Integer, Message> messageRepo) {
        this.userRepo = userRepo;
        this.messageRepo = messageRepo;
        this.messageValidator = MessageValidator.getInstance();
    }

    /**
     * Gets a list of integer ids from a string input
     *
     * @param input the ids split by ,
     * @return the list of ids
     */
    public List<Integer> getIdsFromString(String input) {
        String[] idsArray = input.split(",");
        List<Integer> toIds = new ArrayList<>();
        for (String id : idsArray) {
            toIds.add(Integer.parseInt(id));
        }
        return toIds;
    }

    /**
     * Sends a message
     *
     * @param fromId  the id of the user who sends the message
     * @param toIds   the ids of the users to whom the message is sent
     * @param message the message itself
     * @throws Exception if the operation fails
     */
    public void sendMessage(int fromId, List<Integer> toIds, String message) throws Exception {
        User from = userRepo.findById(fromId);
        List<User> toUsers = new ArrayList<>();
        for (Integer id : toIds) {
            User user = userRepo.findById(id);
            toUsers.add(user);
        }
        Message messageToSend = new Message(from, toUsers, message);
        messageValidator.validate(messageToSend);
        messageRepo.add(messageToSend);
    }

    /**
     * Sends a reply message
     *
     * @param idMessage the id of the message to which the user replies
     * @param idReplier the id of the user who replies
     * @param message   the message itself
     * @throws Exception if the operation fails
     */
    public void sendReplyMessage(int idMessage, int idReplier, String message) throws Exception {
        Message messageToReplyTo = messageRepo.findById(idMessage);
        List<User> to = messageToReplyTo.getTo();
        User replier = userRepo.findById(idReplier);

        if (to.contains(replier)) {
            List<User> receiver = new ArrayList<>();
            receiver.add(messageToReplyTo.getFrom());
            Message messageToSend = new Message(replier, receiver, message);
            messageValidator.validate(messageToSend);
            messageToSend.setReply(messageToReplyTo);
            messageRepo.add(messageToSend);
        } else throw new MessageException("User didn't receive that message!\n");
    }

    /**
     * Gets all the messages
     *
     * @return a collection of all the messages
     */
    public Collection<Message> getAllMessages() {
        return messageRepo.findAll();
    }

    /**
     * Gets all the messages between 2 users in chronological order
     *
     * @param idUser1 the id of the first user
     * @param idUser2 the id of the second user
     * @return a collection of messages
     */
    public Collection<Message> getChat(int idUser1, int idUser2) throws Exception {
        Collection<Message> messages = messageRepo.findAll();
        Stream<Message> stream = messages.stream().
                sorted(Comparator.comparing(Message::getDate));
        List<Message> messagesSorted = stream.toList();
        Collection<Message> chat = new ArrayList<>();

        for (Message msg : messagesSorted) {
            if (msg.getFrom().getId() == idUser1) {
                for (User us : msg.getTo()) {
                    if (us.equals(userRepo.findById(idUser2)))
                        chat.add(msg);
                }
            } else if (msg.getFrom().getId() == idUser2) {
                for (User us : msg.getTo()) {
                    if (us.equals(userRepo.findById(idUser1)))
                        chat.add(msg);
                }
            }
        }
        if (chat.isEmpty()) throw new MessageException("There are no messages between these users!\n");
        return chat;
    }

    /**
     * Sends a reply to all message
     *
     * @param idMessage the id of the message to which the user replies
     * @param idReplier the id of the user who replies to all
     * @param message   the message itself
     * @throws Exception if the operation fails
     */
    public void sendReplyToAll(int idMessage, int idReplier, String message) throws Exception {
        Message messageToReplyTo = messageRepo.findById(idMessage);
        List<User> to = messageToReplyTo.getTo();
        User from = messageToReplyTo.getFrom();
        User replier = userRepo.findById(idReplier);

        if (to.contains(replier)) {
            List<User> send_to = messageToReplyTo.getTo();
            send_to.remove(replier);
            send_to.add(from);
            Message messageToSend = new Message(replier, send_to, message);
            messageValidator.validate(messageToSend);
            messageToSend.setReply(messageToReplyTo);
            messageRepo.add(messageToSend);
        } else throw new MessageException("User didn't receive that message!\n");
    }
}
