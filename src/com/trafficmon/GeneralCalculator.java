package com.trafficmon;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public abstract class GeneralCalculator {
    public abstract BigDecimal calculateCharge(List<ZoneBoundaryCrossing> crossings);
    private SystemServiceInterface systemServices = new SystemService();

    public int minutesBetween(long startTimeMs, long endTimeMs) {
        return (int) Math.ceil((endTimeMs - startTimeMs) / (1000.0 * 60.0));
    }

    public void calculateAllVehicleCharges(Map<Vehicle,List<ZoneBoundaryCrossing>> vehiclesCrossingsMap){
        for (Map.Entry<Vehicle, List<ZoneBoundaryCrossing>>
                vehicleCrossings :vehiclesCrossingsMap.entrySet()) {

           // Vehicle vehicle = vehicleCrossings.getKey();
           // List<ZoneBoundaryCrossing> crossings = vehicleCrossings.getValue();
            calculateOneVehicleCharge(vehicleCrossings.getKey(),vehicleCrossings.getValue());

        }
    }

    public void calculateOneVehicleCharge(Vehicle vehicle,List<ZoneBoundaryCrossing> crossings){
        if (!checkOrderingOf(crossings,vehicle)) {
            systemServices.triggerInvestigation(vehicle);
        }
        else{
            BigDecimal charge = calculateCharge(crossings);
            systemServices.chargeDeduction(vehicle,charge);

        }
    }

    public boolean checkOrderingOf(List<ZoneBoundaryCrossing> crossings, Vehicle vehicle) {

        ZoneBoundaryCrossing lastEvent = crossings.get(0);

        for (ZoneBoundaryCrossing crossing : crossings.subList(1, crossings.size())) {
            if (crossing.timestamp() < lastEvent.timestamp()
                    || crossing instanceof EntryEvent && lastEvent instanceof EntryEvent
                    || crossing instanceof ExitEvent && lastEvent instanceof ExitEvent ) {
                return false;
            }
            lastEvent = crossing;
        }

        return true;
    }
}
