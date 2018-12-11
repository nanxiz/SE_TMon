package com.trafficmon;

import java.util.*;

public class CongestionChargeSystem {
    private final GeneralCalculator newRuleCalculator;
    private final VehiclesCrossingsRecord vehiclesCrossingsRecords = new VehiclesCrossingsRecord();
    private final List<ZoneBoundaryCrossing> eventLog = new ArrayList<ZoneBoundaryCrossing>();


    CongestionChargeSystem(){
        this.newRuleCalculator = new NewRuleCalculator();
    }


    public void vehicleEnteringZone(Vehicle vehicle) {
        eventLog.add(new EntryEvent(vehicle));
    }


    public void vehicleLeavingZone(Vehicle vehicle) {
        if (!previouslyRegistered(vehicle,eventLog)) { return; }
        eventLog.add(new ExitEvent(vehicle));

    }


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
