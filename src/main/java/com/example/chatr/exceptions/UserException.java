package com.example.chatr.exceptions;

public class UserException extends Exception {
    /**
     * Constructor for UserException
     *
     * @param err String of errors
     */
    public UserException(String err) {
        super("User Exception: " + err);
    }
}
