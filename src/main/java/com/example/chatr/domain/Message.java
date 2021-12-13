package com.example.chatr.domain;

import com.example.chatr.utils.Constants;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Integer> {
    private Message reply;
    private User from;
    private List<User> to;
    private String message;
    private LocalDateTime date;

    /**
     * Constructor for Message
     *
     * @param id      the id of the message
     * @param from    the user where the message is from
     * @param to      the user list to whom to send the message
     * @param message the message itself
     */
    public Message(Integer id, User from, List<User> to, String message) {
        super(id);
        this.reply = null;
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = LocalDateTime.now();
    }

    /**
     * Constructor for Message
     *
     * @param from    the user where the message is from
     * @param to      the user list to whom to send the message
     * @param message the message itself
     */
    public Message(User from, List<User> to, String message) {
        super(0);
        this.reply = null;
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = LocalDateTime.now();
    }

    /**
     * Getter for id
     *
     * @return the id of the message
     */
    public Integer getId() {
        return id;
    }

    /**
     * Setter for id
     *
     * @param id generic object ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Modifies the entity
     *
     * @param entity the new entity for the same ID
     */
    @Override
    public void modify(Entity<Integer> entity) {
        Message message = (Message) entity;
        setFrom(message.getFrom());
        setMessage(message.getMessage());
        setTo(message.getTo());
    }

    /**
     * Getter for from
     *
     * @return the 'from' user
     */
    public User getFrom() {
        return from;
    }

    /**
     * Setter for from
     *
     * @param from the new 'from' user
     */
    public void setFrom(User from) {
        this.from = from;
    }

    /**
     * Getter for to
     *
     * @return the 'to' user list
     */
    public List<User> getTo() {
        return to;
    }

    /**
     * Setter for to
     *
     * @param to the new 'to' user list
     */
    public void setTo(List<User> to) {
        this.to = to;
    }

    /**
     * Getter for message
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for message
     *
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter for date
     *
     * @return the date
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Setter for date
     *
     * @param date the new date
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Getter for reply message
     *
     * @return the reply message
     */
    public Message getReply() {
        return reply;
    }

    /**
     * Setter for replay message
     *
     * @param reply sets the message to which the actual message replies
     */
    public void setReply(Message reply) {
        this.reply = reply;
    }

    /**
     * Converts the Message into a string
     *
     * @return the message object as a string
     */
    @Override
    public String toString() {
        if (reply == null) {
            return "Message: " +
                    "id=" + id +
                    ", from=" + from +
                    ", to=" + to +
                    ", message='" + message + '\'' +
                    ", date=" + date.format(Constants.DATE_TIME_FORMATTER);
        }
        return "Message: " +
                "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", date=" + date.format(Constants.DATE_TIME_FORMATTER) +
                ", reply to message: " + reply.getId();
    }
}
