package com.example.chatr.exceptions;

public class FriendshipException extends Exception {
    /**
     * Constructor for FriendshipException
     *
     * @param err String of errors
     */
    public FriendshipException(String err) {
        super("Friendship Exception: " + err);
    }
}
