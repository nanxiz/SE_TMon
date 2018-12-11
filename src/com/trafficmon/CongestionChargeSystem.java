package com.trafficmon;

import java.util.*;

public class CongestionChargeSystem {
    private final GeneralCalculator newRuleCalculator;
    private final VehiclesCrossingsRecord vehiclesCrossingsRecords;
    private final List<ZoneBoundaryCrossing> eventLog;


    CongestionChargeSystem(){
        this.newRuleCalculator = new NewRuleCalculator();
        this.eventLog = new ArrayList<ZoneBoundaryCrossing>();
        this.vehiclesCrossingsRecords = new VehiclesCrossingsRecord();
    }


    public void vehicleEnteringZone(Vehicle vehicle) {
        eventLog.add(new EntryEvent(vehicle));
    }


    public void vehicleLeavingZone(Vehicle vehicle) {
        if (!previouslyRegistered(vehicle,eventLog)) { return; }
        eventLog.add(new ExitEvent(vehicle));

    }

    /**
     * this method is for calculating all the vehicles charge and charge them
     */
    public void calculateCharges() {
        newRuleCalculator.calculateAllVehicleCharges(vehiclesCrossingsRecords.fileEventLogIntoVehiclesRecord(eventLog));
    }


    public  List<ZoneBoundaryCrossing> getEventLog(){
        return eventLog;
    }


    public boolean previouslyRegistered(Vehicle vehicle, List<ZoneBoundaryCrossing> eventLog) {
        for (ZoneBoundaryCrossing crossing : eventLog) {
            if (crossing.getVehicle().equals(vehicle)) { return true; }
        }
        return false;
    }


}
