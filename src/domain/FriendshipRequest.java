package domain;

import java.util.Objects;

public class FriendshipRequest extends Entity<Integer>{
    User sender;
    User receiver;
    String status;

    public FriendshipRequest(User sender, User receiver, String status) {
        super(0);
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }

    public FriendshipRequest(User sender, User receiver) {
        super(0);
        this.sender = sender;
        this.receiver = receiver;
        this.status="PENDING";
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getStatus() {
        return status;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    /**
     * Modifies the entity
     * @param entity the new entity
     */
    @Override
    public void modify(Entity<Integer> entity) {
        FriendshipRequest friendshipReq = (FriendshipRequest) entity;
        setSender(friendshipReq.getSender());
        setReceiver(friendshipReq.getReceiver());
    }


    /**
     * Checks if 2 objects are equal
     * @param o other Object
     * @return true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipRequest friendshipReq = (FriendshipRequest) o;
        return sender.equals(friendshipReq.sender) && receiver.equals(friendshipReq.receiver);
    }

    /**
     * Gets the hash code of the objects
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver);
    }
    @Override
    public String toString() {
        return "FriendshipRequest{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", status='" + status + '\'' +
                '}';
    }
}
