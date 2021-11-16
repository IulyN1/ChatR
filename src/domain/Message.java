package domain;

import java.time.LocalDate;
import java.util.List;

public class Message extends Entity<Integer>{
    private User from;
    private List<User> to;
    private String message;
    private final LocalDate date;

    public Message(Integer id, User from, List<User> to, String message) {
        super(id);
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = LocalDate.now();
    }

    public Integer getId() {
        return id;
    }

    @Override
    public void modify(Entity<Integer> entity) {
        Message message = (Message) entity;
        setFrom(message.getFrom());
        setMessage(message.getMessage());
        setTo(message.getTo());
    }

    public User getFrom() {
        return from;
    }

    public List<User> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public void setTo(List<User> to) {
        this.to = to;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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
