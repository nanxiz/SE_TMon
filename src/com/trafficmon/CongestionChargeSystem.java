package com.trafficmon;

import java.math.BigDecimal;
import java.util.*;




public class CongestionChargeSystem {

    private VehiclesRecord vehiclesRecords = new VehiclesRecord();
    private final Calculator newRuleCalculator = new NewRuleCalculator();
    private final List<ZoneBoundaryCrossing> eventLog = new ArrayList<ZoneBoundaryCrossing>();


    public void vehicleEnteringZone(Vehicle vehicle) {
        eventLog.add(new EntryEvent(vehicle));
    }

    public void vehicleLeavingZone(Vehicle vehicle) {
        if (!vehiclesRecords.previouslyRegistered(vehicle,eventLog)) {
            return;
        }
        eventLog.add(new ExitEvent(vehicle));

    }


    public void calculateCharges() {

        for (Map.Entry<Vehicle, List<ZoneBoundaryCrossing>>
                vehicleCrossings : vehiclesRecords.fileEventLogIntoVehiclesRecord(eventLog).entrySet()) {
            Vehicle vehicle = vehicleCrossings.getKey();
            List<ZoneBoundaryCrossing> crossings = vehicleCrossings.getValue();

            if (vehiclesRecords.checkOrderingOf(crossings,vehicle)){

                BigDecimal charge = newRuleCalculator.calculateChargeForTimeInZone(crossings);

                try {
                    new SystemBehaviors().chargeDetuction(vehicle,charge);
                } catch (InsufficientCreditException ice) {
                    new SystemBehaviors().issuePenalty(vehicle,charge);
                } catch (AccountNotRegisteredException e) {
                    new SystemBehaviors().issuePenalty(vehicle,charge);
                }

            }
        }
    }

    public  List<ZoneBoundaryCrossing> getEventLog(){
        return eventLog;
    }




}
