package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime(); // getTime so we have the milliseconds instead
        long outHour = ticket.getOutTime().getTime(); // getTime so we have the milliseconds instead

        // get in millisecs the difference between the two dates
        long timeDiffInMillis = Math.abs(inHour - outHour);

        // get in minutes now (not in hours since if it's 45 mins it counts as 0 hours)
        long timeDiffInMinutes = TimeUnit.MINUTES.convert(timeDiffInMillis, TimeUnit.MILLISECONDS);

        // finally, convert into decimal hours (ex: 45min = 0.75h)
        double timeDiffFinal = timeDiffInMinutes;
        timeDiffFinal = timeDiffFinal / 60;

        //TODO: Some tests are failing here. Need to check if this logic is correct
        double duration = timeDiffFinal;

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}