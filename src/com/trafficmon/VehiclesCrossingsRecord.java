package com.trafficmon;

import java.math.BigDecimal;
import java.util.*;

public class VehiclesCrossingsRecord implements VehiclesCrossingsRecordInterface {
    private final Map<Vehicle, List<ZoneBoundaryCrossing>> vehiclesCrossingsMap;


    public VehiclesCrossingsRecord(){
        this.vehiclesCrossingsMap = new HashMap<Vehicle, List<ZoneBoundaryCrossing>>();
    }

    @Override
    public Map<Vehicle,List<ZoneBoundaryCrossing>> fileEventLogIntoVehiclesRecord(List<ZoneBoundaryCrossing> eventLog){
        for (ZoneBoundaryCrossing crossing : eventLog) {
            if (!vehiclesCrossingsMap.containsKey(crossing.getVehicle())) {
                vehiclesCrossingsMap.put(crossing.getVehicle(), new ArrayList<ZoneBoundaryCrossing>());
            }
            vehiclesCrossingsMap.get(crossing.getVehicle()).add(crossing);

        }
        return vehiclesCrossingsMap;
    }


    public Map<Vehicle, List<ZoneBoundaryCrossing>> getVehiclesCrossingsMap() {
        return vehiclesCrossingsMap;
    }
}









