package com.trafficmon;

import java.math.BigDecimal;
import java.util.*;




public class CongestionChargeSystem {

    private VehiclesRecord vehiclesRecords = new VehiclesRecord();
    private  VehicleRecordCheck checkVehicleRecord = new VehicleRecordCheck();
    private final NewRuleCalculator newRuleCalculator = new NewRuleCalculator();
    private final List<ZoneBoundaryCrossing> eventLog = new ArrayList<ZoneBoundaryCrossing>();
    //private OperationsTeam operationsTeam;



    public void vehicleEnteringZone(Vehicle vehicle) {
        eventLog.add(new EntryEvent(vehicle));
    }

    public void vehicleLeavingZone(Vehicle vehicle) {
        if (!previouslyRegistered(vehicle)) {
            return;
        }
        eventLog.add(new ExitEvent(vehicle));
    }



    public void calculateCharges() {



        for (Map.Entry<Vehicle, List<ZoneBoundaryCrossing>>
                vehicleCrossings : vehiclesRecords.fileEventLogIntoVehiclesRecord(eventLog).entrySet()) {
            Vehicle vehicle = vehicleCrossings.getKey();
            List<ZoneBoundaryCrossing> crossings = vehicleCrossings.getValue();

            if (checkVehicleRecord.checkOrderingOf(crossings,vehicle)){

                BigDecimal charge = newRuleCalculator.calculateChargeForTimeInZone(crossings,vehicle);

                try {
                    RegisteredCustomerAccountsService.getInstance().accountFor(vehicle).deduct(charge);
                } catch (InsufficientCreditException ice) {
                    OperationsTeam.getInstance().issuePenaltyNotice(vehicle, charge);
                } catch (AccountNotRegisteredException e) {
                    OperationsTeam.getInstance().issuePenaltyNotice(vehicle, charge);
                }

            }
        }
    }

    //figure out a fucking way to change this
    public  List<ZoneBoundaryCrossing> getEventLog(){
        return eventLog;
    }




    private boolean previouslyRegistered(Vehicle vehicle) {
        for (ZoneBoundaryCrossing crossing : eventLog) {
            if (crossing.getVehicle().equals(vehicle)) {
                return true;
            }
        }
        return false;
    }













}
