package com.trafficmon;

import java.util.List;
import java.util.Map;

public interface VehiclesCrossingsRecordInterface {
    Map<Vehicle,List<ZoneBoundaryCrossing>> fileEventLogIntoVehiclesRecord(List<ZoneBoundaryCrossing> eventLog);

    //Map<Vehicle, List<ZoneBoundaryCrossing>> getVehiclesCrossingsMap();
}
