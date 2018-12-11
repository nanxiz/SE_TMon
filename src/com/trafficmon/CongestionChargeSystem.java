package com.trafficmon;

import java.util.*;




public class CongestionChargeSystem {
    private final GeneralCalculator newRuleCalculator;
    private AccountsService accountsService = RegisteredCustomerAccountsService.getInstance();
    private PenaltiesService penaltiesService = OperationsTeam.getInstance();

    CongestionChargeSystem(PenaltiesService penaltiesService){
        this.penaltiesService = penaltiesService;
        this.newRuleCalculator = new NewRuleCalculator();
    }
    private SystemServiceInterface systemBehaviors = new SystemService();
    private final VehiclesCrossingsRecord vehiclesCrossingsRecords = new VehiclesCrossingsRecord();
    //private final Calculator newRuleCalculator = new NewRuleCalculator();
    private final List<ZoneBoundaryCrossing> eventLog = new ArrayList<ZoneBoundaryCrossing>();


    public void vehicleEnteringZone(Vehicle vehicle) {
        eventLog.add(new EntryEvent(vehicle));
    }

    public void vehicleLeavingZone(Vehicle vehicle) {
        if (!previouslyRegistered(vehicle,eventLog)) {
            return;
        }
        eventLog.add(new ExitEvent(vehicle));

    }

    /*
    public void calculateAllVehicleCharges() {

        for (Map.Entry<Vehicle, List<ZoneBoundaryCrossing>>
                vehicleCrossings : vehiclesCrossingsRecords.fileEventLogIntoVehiclesRecord(eventLog).entrySet()) {
            Vehicle vehicle = vehicleCrossings.getKey();
            List<ZoneBoundaryCrossing> crossings = vehicleCrossings.getValue();

            if (vehiclesCrossingsRecords.checkOrderingOf(crossings,vehicle)){

                BigDecimal charge = newRuleCalculator.calculateChargeForTimeInZone(crossings);
                operating(vehicle, charge);
            }else{
                penaltiesService.triggerInvestigationInto(vehicle);
            }
        }
    }
    */

    public void calculateCharges() {

        //vehiclesCrossingsRecords.calculateAllVehicleCharges(newRuleCalculator);
        newRuleCalculator.calculateAllVehicleCharges(vehiclesCrossingsRecords.fileEventLogIntoVehiclesRecord(eventLog));
    }



/*
    public void attemptToDeductCharges(Vehicle vehicle, BigDecimal charge) {
        try {
            new SystemService().chargeDetuction(vehicle,charge);
        } catch (InsufficientCreditException | AccountNotRegisteredException e) {
            new SystemService().issuePenalty(vehicle,charge);
        }
    }
*/

    public  List<ZoneBoundaryCrossing> getEventLog(){
        return eventLog;
    }


    public boolean previouslyRegistered(Vehicle vehicle, List<ZoneBoundaryCrossing> eventLog) {
        for (ZoneBoundaryCrossing crossing : eventLog) {
            if (crossing.getVehicle().equals(vehicle)) {
                return true;
            }
        }
        return false;
    }


}
