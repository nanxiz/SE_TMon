package com.trafficmon;

import java.math.BigDecimal;
import java.util.*;

public class VehiclesRecord {
    private final Map<Vehicle, List<ZoneBoundaryCrossing>> crossingsByVehicle;
    //Map<Vehicle, List<ZoneBoundaryCrossing>> crossingsByVehicle = new HashMap<Vehicle, List<ZoneBoundaryCrossing>>();
    public VehiclesRecord(){ this.crossingsByVehicle = new HashMap<Vehicle, List<ZoneBoundaryCrossing>>();}


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









}
