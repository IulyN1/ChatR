package com.example.chatr.exceptions;

public class MessageException extends Exception {
    /**
     * Constructor for MessageException
     *
     * @param err String of errors
     */
    public MessageException(String err) {
        super("Message Exception: " + err);
    }
}
