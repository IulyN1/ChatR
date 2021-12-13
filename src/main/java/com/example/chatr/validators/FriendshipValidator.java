package com.example.chatr.validators;

import com.example.chatr.domain.Friendship;
import com.example.chatr.exceptions.FriendshipException;
import com.example.chatr.exceptions.UserException;

public class FriendshipValidator implements StrategyValidator<Friendship> {
    private final static FriendshipValidator friendshipValidator = new FriendshipValidator();

    /**
     * Private constructor for FriendshipValidator
     */
    private FriendshipValidator() {
    }

    /**
     * Gets the instance of validator
     *
     * @return the FriendshipValidator instance
     */
    public static FriendshipValidator getInstance() {
        return friendshipValidator;
    }

    /**
     * Validates a friendship
     *
     * @param friendship friendship to be validated
     * @throws FriendshipException if the friendship is not valid
     */
    @Override
    public void validate(Friendship friendship) throws FriendshipException {
        String err = "";
        if (friendship.getId() < 0)
            err += "Invalid ID!\n";

        try {
            UserValidator.getInstance().validate(friendship.getUser1());
        } catch (UserException error) {
            err += "First user: \n";
            err += error.getMessage();
        }

        try {
            UserValidator.getInstance().validate(friendship.getUser2());
        } catch (UserException error) {
            err += "Second user: \n";
            err += error.getMessage();
        }

        if (!err.equals(""))
            throw new FriendshipException(err);
    }
}
