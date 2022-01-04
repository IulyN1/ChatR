package com.example.chatr.exceptions;

public class EventException  extends Exception{
    public EventException(String err) {
        super("EventException: " + err);
    }
}
