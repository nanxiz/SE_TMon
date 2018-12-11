package com.trafficmon;

import java.util.*;

public class CongestionChargeSystem {
    private final GeneralCalculator calculator;
    private final List<ZoneBoundaryCrossing> eventLog;
    private boolean calculated;


    CongestionChargeSystem(){
        this.calculator = new NewRuleCalculator();
        this.eventLog = new ArrayList<ZoneBoundaryCrossing>();
        this.calculated = false;
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
        calculated = calculator.calculateAllVehicleCharges(buildVehicleMap(eventLog));
    }


    /**
     *This method takes eventLog and return
     * map of vehicles and their corresponding crossings
     */
    public Map<Vehicle,List<ZoneBoundaryCrossing>> buildVehicleMap(List<ZoneBoundaryCrossing> eventLogg){
        Map<Vehicle, List<ZoneBoundaryCrossing>> crossingsByVehicle = new HashMap<Vehicle, List<ZoneBoundaryCrossing>>();
        for (ZoneBoundaryCrossing crossing : eventLogg) {
            if (!crossingsByVehicle.containsKey(crossing.getVehicle())) {
                crossingsByVehicle.put(crossing.getVehicle(), new ArrayList<ZoneBoundaryCrossing>());
            }
            crossingsByVehicle.get(crossing.getVehicle()).add(crossing);

        }
        return crossingsByVehicle;
    }



    public boolean previouslyRegistered(Vehicle vehicle, List<ZoneBoundaryCrossing> eventLog) {
        for (ZoneBoundaryCrossing crossing : eventLog) {
            if (crossing.getVehicle().equals(vehicle)) { return true; }
        }
        return false;
    }

    public  List<ZoneBoundaryCrossing> getEventLog(){
        return eventLog;
    }

    public boolean whetherCalculated(){
        return calculated;
    }


}
