package domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Message extends Entity<Integer>{
    private User from;
    private List<User> to;
    private String message;
    private LocalDate date;

    /**
     * Constructor for Message
     * @param id the id of the message
     * @param from the user where the message is from
     * @param to the user list to whom to send the message
     * @param message the message itself
     */
    public Message(Integer id, User from, List<User> to, String message) {
        super(id);
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = LocalDate.now();
    }

    /**
     * Constructor for Message
     * @param from the user where the message is from
     * @param to the user list to whom to send the message
     * @param message the message itself
     */
    public Message(User from, List<User> to, String message) {
        super(0);
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = LocalDate.now();
    }

    /**
     * Getter for id
     * @return the id of the message
     */
    public Integer getId() {
        return id;
    }

    /**
     * Modifies the entity
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
     * @return the 'from' user
     */
    public User getFrom() {
        return from;
    }

    /**
     * Getter for to
     * @return the 'to' user list
     */
    public List<User> getTo() {
        return to;
    }

    /**
     * Getter for message
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Getter for date
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Setter for id
     * @param id generic object ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Setter for from
     * @param from the new 'from' user
     */
    public void setFrom(User from) {
        this.from = from;
    }

    /**
     * Setter for to
     * @param to the new 'to' user list
     */
    public void setTo(List<User> to) {
        this.to = to;
    }

    /**
     * Setter for message
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Setter for date
     * @param date the new date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Converts the Message into a string
     * @return the message object as a string
     */
    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}
