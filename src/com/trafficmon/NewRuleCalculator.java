package com.trafficmon;


import java.math.BigDecimal;
import java.util.List;
import org.joda.time.*;


public class CalculateNewCharges {
    public BigDecimal calculateChargeForTimeInZone(List<ZoneBoundaryCrossing> crossings) {

        ZoneBoundaryCrossing lastEvent = crossings.get(0);

        int timeInZone = 0;
        long enterTime = lastEvent.timestamp(); // to store the updated time after each enter

        BigDecimal charge = BigDecimal.valueOf(0);

        if (checkIfTimeBeforeTwo(enterTime)) {charge = BigDecimal.valueOf(6);}
        else {charge = BigDecimal.valueOf(4);}

        for (ZoneBoundaryCrossing crossing : crossings.subList(1, crossings.size())) {

            if (crossing instanceof ExitEvent) {
               timeInZone += minutesBetween(enterTime,crossing.timestamp());
               if (timeInZone>4*60){
                   charge = BigDecimal.valueOf(12);
                   break;
               }

            }
            if (crossing instanceof EntryEvent) {
                if (minutesBetween(enterTime,crossing.timestamp())>4*60){
                    if (checkIfTimeBeforeTwo(crossing.timestamp())){
                        charge = charge.add(BigDecimal.valueOf(6));
                    }
                }
            }



            lastEvent = crossing;
        }














        return charge;
    }

    private boolean checkIfTimeBeforeTwo(long time){
        DateTime InitTime = new DateTime(time);

        if (InitTime.getHourOfDay()<14) {
            return true;

        }
        return false;
    }

    //以后改成interface
    private int minutesBetween(long startTimeMs, long endTimeMs) {
        return (int) Math.ceil((endTimeMs - startTimeMs) / (1000.0 * 60.0));
    }




}
