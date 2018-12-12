package com.trafficmon;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public abstract class GeneralCalculator {
    private SystemServiceInterface systemServices;

    public GeneralCalculator(){
        this.systemServices =  new SystemService();
    }

    public abstract BigDecimal calculateCharge(List<ZoneBoundaryCrossing> crossings);

    public int minutesBetween(long startTimeMs, long endTimeMs) {
        return (int) Math.ceil((endTimeMs - startTimeMs) / (1000.0 * 60.0));
    }

    public boolean calculateAllVehicleCharges(Map<Vehicle, List<ZoneBoundaryCrossing>> vehiclesCrossingsMap){
        for (Map.Entry<Vehicle, List<ZoneBoundaryCrossing>>
                vehicleCrossings :vehiclesCrossingsMap.entrySet()) {
            calculateAndChargeOneVehicle(vehicleCrossings.getKey(),vehicleCrossings.getValue());
        }
        return true;
    }

    private void calculateAndChargeOneVehicle(Vehicle vehicle, List<ZoneBoundaryCrossing> crossings){
        if (!checkOrderingOf(crossings,vehicle)) {
            systemServices.triggerInvestigation(vehicle);
        }
        else{
            BigDecimal charge = calculateCharge(crossings);
            systemServices.chargeDeduction(vehicle,charge);

        }
    }

    boolean checkOrderingOf(List<ZoneBoundaryCrossing> crossings, Vehicle vehicle) {

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

    //for testing private calculateAndChargeOneVehicle() method
    public void calculateForOneVehicle(Vehicle vehicle,List<ZoneBoundaryCrossing> crossing){
        calculateAndChargeOneVehicle(vehicle,crossing);

    }
}
