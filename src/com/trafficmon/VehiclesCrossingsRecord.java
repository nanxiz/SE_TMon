package com.trafficmon;

import java.util.*;

public class VehiclesCrossingsRecord implements VehiclesCrossingsRecordInterface {
    private final Map<Vehicle, List<ZoneBoundaryCrossing>> CrossingsByVehicle;


    public VehiclesCrossingsRecord(){
        this.CrossingsByVehicle = new HashMap<Vehicle, List<ZoneBoundaryCrossing>>();
    }

    @Override
    public Map<Vehicle,List<ZoneBoundaryCrossing>> fileEventLogIntoVehiclesRecord(List<ZoneBoundaryCrossing> eventLog){
        for (ZoneBoundaryCrossing crossing : eventLog) {
            if (!CrossingsByVehicle.containsKey(crossing.getVehicle())) {
                CrossingsByVehicle.put(crossing.getVehicle(), new ArrayList<ZoneBoundaryCrossing>());
            }
            CrossingsByVehicle.get(crossing.getVehicle()).add(crossing);

        }
        return CrossingsByVehicle;
    }

    public List<ZoneBoundaryCrossing> getCertainVehicleCrossingRecord(Vehicle vehicle){
        return CrossingsByVehicle.getOrDefault(vehicle, null);
    }


    public Map<Vehicle, List<ZoneBoundaryCrossing>> getCrossingsByVehicle() {
        return CrossingsByVehicle;
    }
}









