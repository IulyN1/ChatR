package com.example.chatr.validators;

import com.example.chatr.domain.Account;
import com.example.chatr.domain.Event;
import com.example.chatr.domain.FriendshipRequest;
import com.example.chatr.exceptions.AccountException;

public class EventValidator  implements StrategyValidator<Event> {
    private final static EventValidator eventValidator= new EventValidator();

    private EventValidator() {
    }
    public static EventValidator getInstance() {
        return eventValidator;
    }


    @Override
    public void validate(Event event) throws Exception {
        String err = "";
        if (event.getName() == null)
            err = err + "Empty event!\n";
        if (event.getDate() == null)
            err = err + "Empty  date!\n";
        if (event.getId() < 0)
            err = err + "Invalid id!\n";
        if (!err.equals(""))
            throw new AccountException(err);
    }
}



