package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        calculateFare(ticket, false);
    }

    public void calculateFare(Ticket ticket, boolean discount){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime(); // getTime so we have the milliseconds instead
        long outHour = ticket.getOutTime().getTime(); // getTime so we have the milliseconds instead

        // get in millisecs the difference between the two dates
        long timeDiffInMillis = Math.abs(outHour - inHour);

        // get in minutes now (not in hours since if it's 45 mins it counts as 0 hours)
        long timeDiffInMinutes = TimeUnit.MINUTES.convert(timeDiffInMillis, TimeUnit.MILLISECONDS);

        // finally, convert into decimal hours (ex: 45min = 0.75h)
        double timeDiffFinal = timeDiffInMinutes;
        timeDiffFinal = timeDiffFinal / 60;

        double duration = timeDiffFinal;
        double ticketPrice;

        // if the time parked is less than 30min (0.5h), the ticket is free, otherwise calculate accordingly
        if (duration < 0.5) {
            ticket.setPrice(0);
        } else {
            switch (ticket.getParkingSpot().getParkingType()){
                case CAR: {
                    ticketPrice = duration * Fare.CAR_RATE_PER_HOUR;
                    break;
                }
                case BIKE: {
                    ticketPrice = duration * Fare.BIKE_RATE_PER_HOUR;
                    break;
                }
                default: throw new IllegalArgumentException("Unkown Parking Type");
            }

            // if there is a discount coupon, reduce by 5% the price
            if (discount == true) {
                ticketPrice = ticketPrice * 0.95;
            }

            ticket.setPrice(ticketPrice);
        }
    }
}