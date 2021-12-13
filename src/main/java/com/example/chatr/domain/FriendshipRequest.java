package com.example.chatr.domain;

import java.util.Objects;

public class FriendshipRequest extends Entity<Integer> {
    User sender;
    User receiver;
    String status;
    String date;

    public FriendshipRequest(User sender, User receiver, String status) {
        super(0);
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }

    public FriendshipRequest(User sender, User receiver, String status, String date) {
        super(0);
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
        this.date = date;
    }

    public FriendshipRequest(User sender, User receiver) {
        super(0);
        this.sender = sender;
        this.receiver = receiver;
        this.status = "PENDING";
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Modifies the entity
     *
     * @param entity the new entity
     */
    @Override
    public void modify(Entity<Integer> entity) {
        FriendshipRequest friendshipReq = (FriendshipRequest) entity;
        setSender(friendshipReq.getSender());
        setReceiver(friendshipReq.getReceiver());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipRequest that = (FriendshipRequest) o;
        return sender.equals(that.sender) && receiver.equals(that.receiver) && status.equals(that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver, status);
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