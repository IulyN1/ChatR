package com.example.chatr.domain;

import java.util.Objects;

public class User extends Entity<Integer> {
    private String firstName;
    private String lastName;

    /**
     * Constructor for User
     *
     * @param id        Integer id
     * @param firstName String first name of user
     * @param lastName  String last name of user
     */
    public User(Integer id, String firstName, String lastName) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Constructor for User
     *
     * @param firstName String first name of user
     * @param lastName  String last name of user
     */
    public User(String firstName, String lastName) {
        super(0);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Gets the first name
     *
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets a new first name to the user
     *
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name
     *
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets a new last name for the user
     *
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Modifies the user with a new one
     *
     * @param entity the new entity
     */
    @Override
    public void modify(Entity<Integer> entity) {
        User user = (User) entity;
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
    }

    /**
     * Checks if 2 objects are equals
     *
     * @param o other Object
     * @return true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) &&
                Objects.equals(id, that.id);
    }

    /**
     * Gets the hash code of the object
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    /**
     * @return the printable String version of the User
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
