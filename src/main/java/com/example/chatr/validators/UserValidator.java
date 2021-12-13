package com.example.chatr.validators;

import com.example.chatr.domain.User;
import com.example.chatr.exceptions.UserException;

public class UserValidator implements StrategyValidator<User> {
    private final static UserValidator userValidator = new UserValidator();

    /**
     * Private constructor for UserValidator
     */
    private UserValidator() {
    }

    /**
     * Gets the instance of the validator
     *
     * @return the instance of UserValidator
     */
    public static UserValidator getInstance() {
        return userValidator;
    }

    /**
     * Validates an user
     *
     * @param user user to be validated
     * @throws UserException if the user is not valid
     */
    @Override
    public void validate(User user) throws UserException {
        String err = "";
        if (user.getId() < 0)
            err += "Invalid ID!\n";
        if (user.getFirstName().equals(""))
            err += "Invalid first name!\n";
        if (user.getLastName().equals(""))
            err += "Invalid last name!\n";

        if (!err.equals(""))
            throw new UserException(err);
    }
}
