package com.trafficmon;


import java.math.BigDecimal;
import java.util.List;

import org.joda.time.*;


public class NewRuleCalculator extends GeneralCalculator {

    private static final int TIME_DETERMINATION_POINT = 14;
    private static final int CHARGE_BEFORE_2PM = 6;
    private static final int CHARGE_AFTER_2PM = 4;
    private static final int MAXIMUM_ONE_CHARGE = 12;
    private static final int MINUTE_IN_ZONE_LIMIT = 240;

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
                if (timeInZone> MINUTE_IN_ZONE_LIMIT){ //240 represents 4 hours
                    charge = MAXIMUM_ONE_CHARGE;
                    break;
                }

            }
            if (crossing instanceof EntryEvent) {
                if (minutesBetween(lastEvent.timestamp(),crossing.timestamp())> MINUTE_IN_ZONE_LIMIT){
                    charge += checkIfTimeBeforeTwo(crossing.timestamp());

                }
            }

            lastEvent = crossing;
        }



        return BigDecimal.valueOf(charge);
    }

    public int checkIfTimeBeforeTwo(long time){
        DateTime comeInTime = new DateTime(time);
        return (comeInTime.getHourOfDay()<TIME_DETERMINATION_POINT)? CHARGE_BEFORE_2PM : CHARGE_AFTER_2PM;

    }




}
