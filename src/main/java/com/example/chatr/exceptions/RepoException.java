package com.example.chatr.exceptions;

public class RepoException extends Exception {
    /**
     * Constructor for RepoException
     *
     * @param err String of errors
     */
    public RepoException(String err) {
        super("Repository Exception: " + err);
    }
}
