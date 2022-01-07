package com.example.chatr.domain;

public class UserDTO extends User{
    public UserDTO(Integer id, String firstName, String lastName) {
        super(id, firstName, lastName);
    }

    public UserDTO(String firstName, String lastName) {
        super(firstName, lastName);
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}
