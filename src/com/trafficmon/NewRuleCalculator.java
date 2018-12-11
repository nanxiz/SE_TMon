package com.trafficmon;


import java.math.BigDecimal;
import java.util.List;

import org.joda.time.*;


public class NewRuleCalculator extends GeneralCalculator {

    public static final int TIME_DETERMINATION_POINT = 14;

    @Override
    public BigDecimal calculateCharge(List<ZoneBoundaryCrossing> crossings) {
        return calculate(crossings);
    }

    private BigDecimal calculate(List<ZoneBoundaryCrossing> crossings){
        ZoneBoundaryCrossing lastEvent = crossings.get(0);

        int timeInZone = 0;

        int charge = 0;

        charge = checkIfTimeBeforeTwo(lastEvent.timestamp());

        for (ZoneBoundaryCrossing crossing : crossings.subList(1, crossings.size())) {

            if (crossing instanceof ExitEvent) {
                timeInZone += minutesBetween(lastEvent.timestamp(),crossing.timestamp());
                if (timeInZone>240){ //240 represents 4 hours
                    charge = 12;
                    break;
                }

            }
            if (crossing instanceof EntryEvent) {
                if (minutesBetween(lastEvent.timestamp(),crossing.timestamp())>240){
                    charge += checkIfTimeBeforeTwo(crossing.timestamp());

                }
            }

            lastEvent = crossing;
        }



        return BigDecimal.valueOf(charge);
    }

    public int checkIfTimeBeforeTwo(long time){
        DateTime comeInTime = new DateTime(time);
        return (comeInTime.getHourOfDay()<TIME_DETERMINATION_POINT)? 6 : 4;

    }




}
