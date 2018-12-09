package com.trafficmon;


import java.math.BigDecimal;
import java.util.List;
import org.joda.time.*;


public class NewRuleCalculator {


    public void calculateChargeForTimeInZone(List<ZoneBoundaryCrossing> crossings,Vehicle vehicle) {

        ZoneBoundaryCrossing lastEvent = crossings.get(0);

        int timeInZone = 0;

        int charge = 0;

        charge = checkIfTimeBeforeTwo(lastEvent.timestamp(),charge);

        for (ZoneBoundaryCrossing crossing : crossings.subList(1, crossings.size())) {

            if (crossing instanceof ExitEvent) {
               timeInZone += minutesBetween(lastEvent.timestamp(),crossing.timestamp());
               if (timeInZone>4*60){
                   charge = 12;
                   break;
               }

            }
            if (crossing instanceof EntryEvent) {
                if (minutesBetween(lastEvent.timestamp(),crossing.timestamp())>4*60){
                    charge = checkIfTimeBeforeTwo(lastEvent.timestamp(),charge);

                }
            }

            lastEvent = crossing;
        }

        try {
            RegisteredCustomerAccountsService.getInstance().accountFor(vehicle).deduct(BigDecimal.valueOf(charge));
        } catch (InsufficientCreditException ice) {
            OperationsTeam.getInstance().issuePenaltyNotice(vehicle, BigDecimal.valueOf(charge));
        } catch (AccountNotRegisteredException e) {
            OperationsTeam.getInstance().issuePenaltyNotice(vehicle, BigDecimal.valueOf(charge));
        }

        //return BigDecimal.valueOf(charge);
    }

    private int checkIfTimeBeforeTwo(long time,int charge){
        DateTime InitTime = new DateTime(time);
        return (InitTime.getHourOfDay()<14)? charge+6 : charge+4;

    }



    private int minutesBetween(long startTimeMs, long endTimeMs) {
        return (int) Math.ceil((endTimeMs - startTimeMs) / (1000.0 * 60.0));
    }




}