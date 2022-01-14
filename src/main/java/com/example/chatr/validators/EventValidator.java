package com.example.chatr.validators;

import com.example.chatr.domain.Event;
import com.example.chatr.exceptions.EventException;

import java.text.SimpleDateFormat;

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
        SimpleDateFormat formatter5=new SimpleDateFormat("dd MMM yyyy");
        /*
        try {
            Date date5=formatter5.parse(event.getDate());
            Date currentDate = new Date();
            if(date5.getDay()<currentDate.getDay()&&date5.getMonth()<=currentDate.getMonth()&&
            date5.getYear()<=date5.getYear()){
                err=err+"The event cannot be scheduled in the past!\n";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

         */
        if (!err.equals(""))
            throw new EventException(err);
    }
}



