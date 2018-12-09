package com.trafficmon;

import java.math.BigDecimal;
import java.util.*;

public class VehiclesRecord {
    private final Map<Vehicle, List<ZoneBoundaryCrossing>> crossingsByVehicle;
    //Map<Vehicle, List<ZoneBoundaryCrossing>> crossingsByVehicle = new HashMap<Vehicle, List<ZoneBoundaryCrossing>>();
    //private SystemBehaviors systemBehaviors;
    public VehiclesRecord(){
        this.crossingsByVehicle = new HashMap<Vehicle, List<ZoneBoundaryCrossing>>();
        //this.systemBehaviors = new SystemBehaviors();
    }

    //figure out a way to deal with one day record detection deletion
    public Map<Vehicle, List<ZoneBoundaryCrossing>> fileEventLogIntoVehiclesRecord(List<ZoneBoundaryCrossing> eventLog){
        for (ZoneBoundaryCrossing crossing : eventLog) {
            if (!crossingsByVehicle.containsKey(crossing.getVehicle())) {
                crossingsByVehicle.put(crossing.getVehicle(), new ArrayList<ZoneBoundaryCrossing>());
            }
        crossingsByVehicle.get(crossing.getVehicle()).add(crossing);
        }
        //delete later
        //System.out.println(crossingsByVehicle);
        return crossingsByVehicle;
    }

    public boolean checkOrderingOf(List<ZoneBoundaryCrossing> crossings, Vehicle vehicle) {

        ZoneBoundaryCrossing lastEvent = crossings.get(0);

        for (ZoneBoundaryCrossing crossing : crossings.subList(1, crossings.size())) {
            if (crossing.timestamp() < lastEvent.timestamp()
                    || crossing instanceof EntryEvent && lastEvent instanceof EntryEvent
                    || crossing instanceof ExitEvent && lastEvent instanceof ExitEvent ) {
                //OperationsTeam.getInstance().triggerInvestigationInto(vehicle);
                new SystemBehaviors().triggerInvestigation(vehicle);
                return false;
            }
            lastEvent = crossing;
        }

        return true;
    }


    public boolean previouslyRegistered(Vehicle vehicle,List<ZoneBoundaryCrossing>eventLog) {
        for (ZoneBoundaryCrossing crossing : eventLog) {
            if (crossing.getVehicle().equals(vehicle)) {
                return true;
            }
        }
        return false;
    }








}
