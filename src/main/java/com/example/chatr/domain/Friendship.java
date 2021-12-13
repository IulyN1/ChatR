package com.example.chatr.domain;

import java.util.Objects;

public class Friendship extends Entity<Integer> {
    User user1;
    User user2;
    String friendshipDate;

    /**
     * Constructor for Friendship
     *
     * @param user1          the first user
     * @param user2          the second user
     * @param friendshipDate
     */
    public Friendship(User user1, User user2, String friendshipDate) {
        super(0);
        this.user1 = user1;
        this.user2 = user2;
        this.friendshipDate = friendshipDate;
    }

    /**
     * Constructor for Friendship
     *
     * @param id    the id of the friendship
     * @param user1 the first user
     * @param user2 the second user
     */
    public Friendship(Integer id, User user1, User user2) {
        super(id);
        this.user1 = user1;
        this.user2 = user2;
    }

    /**
     * Gets the first user of the friendship
     *
     * @return the first user
     */
    public User getUser1() {
        return user1;
    }

    /**
     * Sets the first user of the friendship
     *
     * @param user1 the new first user
     */
    public void setUser1(User user1) {
        this.user1 = user1;
    }

    /**
     * Gets the second user of the friendship
     *
     * @return the second user
     */
    public User getUser2() {
        return user2;
    }

    /**
     * Sets the second user of the friendship
     *
     * @param user2 the second user
     */
    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public String getFriendshipDate() {
        return friendshipDate;
    }

    /**
     * Modifies the friendshipDate
     *
     * @param friendshipDate
     */
    public void setFriendshipDate(String friendshipDate) {
        this.friendshipDate = friendshipDate;
    }

    /**
     * Modifies the entity
     *
     * @param entity the new entity
     */
    @Override
    public void modify(Entity<Integer> entity) {
        Friendship friendship = (Friendship) entity;
        setUser1(friendship.getUser1());
        setUser2(friendship.getUser2());
    }


    /**
     * Checks if 2 objects are equal
     *
     * @param o other Object
     * @return true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship friendship = (Friendship) o;
        return user1.equals(friendship.user1) && user2.equals(friendship.user2);
    }

    /**
     * Gets the hash code of the objects
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(user1, user2);
    }

    /**
     * @return the printable String version of Friendship
     */
    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + id +
                ", user1=" + user1 +
                ", user2=" + user2 +
                ", friendshipDate=" + friendshipDate +
                '}';
    }
}
