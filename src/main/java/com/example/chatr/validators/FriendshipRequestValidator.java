package com.example.chatr.validators;


import com.example.chatr.domain.FriendshipRequest;
import com.example.chatr.exceptions.FriendshipRequestException;
import com.example.chatr.exceptions.UserException;

public class FriendshipRequestValidator implements StrategyValidator<FriendshipRequest> {
    private final static FriendshipRequestValidator friendshipRequestValidator = new FriendshipRequestValidator();

    private FriendshipRequestValidator() {
    }

    public static FriendshipRequestValidator getInstance() {
        return friendshipRequestValidator;
    }

    @Override
    public void validate(FriendshipRequest friendshipRequest) throws FriendshipRequestException {
        String err = "";
        if (friendshipRequest.getId() < 0)
            err += "Invalid ID!\n";

        try {
            UserValidator.getInstance().validate(friendshipRequest.getSender());
        } catch (UserException error) {
            err += "Sender: \n";
            err += error.getMessage();
        }

        try {
            UserValidator.getInstance().validate(friendshipRequest.getReceiver());
        } catch (UserException error) {
            err += "Receiver: \n";
            err += error.getMessage();
        }

        if (!err.equals(""))
            throw new FriendshipRequestException(err);
    }
}

