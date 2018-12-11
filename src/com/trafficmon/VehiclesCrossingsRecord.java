package com.trafficmon;

import java.util.*;

public class VehiclesCrossingsRecord implements VehiclesCrossingsRecordInterface {
    private final Map<Vehicle, List<ZoneBoundaryCrossing>> CrossingsByVehicle;

    //this is used to store the hash map recording all the vehicle and their crossings
    public VehiclesCrossingsRecord(){
        this.CrossingsByVehicle = new HashMap<Vehicle, List<ZoneBoundaryCrossing>>();
    }

    @Override
    /**this method take unordered eventlog for all vehicles
     *to a hashmap with Vehicle as key and its own crossings as value
     */
    public Map<Vehicle,List<ZoneBoundaryCrossing>> fileEventLogIntoVehiclesRecord(List<ZoneBoundaryCrossing> eventLog){
        for (ZoneBoundaryCrossing crossing : eventLog) {
            if (!CrossingsByVehicle.containsKey(crossing.getVehicle())) {
                CrossingsByVehicle.put(crossing.getVehicle(), new ArrayList<ZoneBoundaryCrossing>());
            }
            CrossingsByVehicle.get(crossing.getVehicle()).add(crossing);

        }
        return CrossingsByVehicle;
    }

    //get crossings record of one vehicle
    public List<ZoneBoundaryCrossing> getCertainVehicleCrossingRecord(Vehicle vehicle){
        return CrossingsByVehicle.getOrDefault(vehicle, null);
    }

    //get the crossings records of all vehicle
    public Map<Vehicle, List<ZoneBoundaryCrossing>> getCrossingsByVehicle() {
        return CrossingsByVehicle;
    }
}









