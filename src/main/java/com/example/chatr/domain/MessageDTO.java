package com.example.chatr.domain;

import java.util.List;

public class MessageDTO extends Message {
    /**
     * Constructor for MessageDTO
     *
     * @param id      the id of the message
     * @param from    the user where the message is from
     * @param to      the users list to whom the message is for
     * @param message the message itself
     */
    public MessageDTO(Integer id, User from, List<User> to, String message) {
        super(id, from, to, message);
    }

    /**
     * Constructor for MessageDTO
     *
     * @param from    the user where the message is from
     * @param to      the users list to whom the message is for
     * @param message the message itself
     */
    public MessageDTO(User from, List<User> to, String message) {
        super(from, to, message);
    }

    /**
     * Converts the MessageDTO to a string
     *
     * @return the MessageDTO as a string
     */
    @Override
    public String toString() {
        return getMessage();
    }
}
