package com.example.chatr.validators;

import com.example.chatr.domain.Message;
import com.example.chatr.exceptions.MessageException;

public class MessageValidator implements StrategyValidator<Message> {
    private final static MessageValidator messageValidator = new MessageValidator();

    /**
     * Private constructor for MessageValidator
     */
    private MessageValidator() {
    }

    /**
     * Gets the instance of the validator
     *
     * @return the instance of MessageValidator
     */
    public static MessageValidator getInstance() {
        return messageValidator;
    }

    /**
     * Validates a message
     *
     * @param message message to be validated
     * @throws MessageException if the message is not valid
     */
    @Override
    public void validate(Message message) throws MessageException {
        String err = "";
        if (message.getTo().contains(message.getFrom())) {
            err += "User can't send message to self!\n";
        }
        if (message.getTo().size() < 1) {
            err += "Invalid users to send message!\n";
        }
        if (message.getMessage().equals("")) {
            err += "Invalid message!\n";
        }

        if (!err.equals(""))
            throw new MessageException(err);
    }
}
