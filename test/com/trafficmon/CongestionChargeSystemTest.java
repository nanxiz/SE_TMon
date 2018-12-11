package com.trafficmon;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;




public class CongestionChargeSystemTest {
    private final Vehicle vehicle1 = Vehicle.withRegistration("A123 XYZ");
    private final Vehicle vehicle2 = Vehicle.withRegistration("J091 4PY");
    private CongestionChargeSystem ccsystem = new CongestionChargeSystem();


    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    private VehiclesCrossingsRecordInterface vehiclesCrossingsRecord = context.mock(VehiclesCrossingsRecordInterface.class);




    /**
     * Check the size of eventLog
     * If a vehicle enters and leaves,
     * The eventLog will update with 2 events
     */
    @Test
    public void checkEventLogSize() {
        ccsystem.vehicleEnteringZone(vehicle1);
        ccsystem.vehicleLeavingZone(vehicle1);
        Assert.assertEquals(ccsystem.getEventLog().size(), 2);
    }


    /**
     * Check if the eventLog is updated correctly
     * If different vehicles enter and leave,
     * The eventLog will record the correct vehicle and event
     */
    @Test
    public void checkEventLogEntries(){
        ccsystem.vehicleEnteringZone(vehicle1);
        ccsystem.vehicleEnteringZone(vehicle2);
        ccsystem.vehicleLeavingZone(vehicle1);
        Assert.assertEquals(ccsystem.getEventLog().get(0).getVehicle(), vehicle1);
        Assert.assertEquals(ccsystem.getEventLog().get(1).getVehicle(), vehicle2);
        Assert.assertTrue(ccsystem.getEventLog().get(0) instanceof EntryEvent);
        Assert.assertTrue(ccsystem.getEventLog().get(2) instanceof ExitEvent);
    }




    /**
     * Check if the entering vehicle registers correctly
     * vehicle1 in here is registered while vehicle2 is not registered
     */
    @Test
    public void checkIfPreviouslyRegistered(){
        List<ZoneBoundaryCrossing> eventLog=new ArrayList<>();
        eventLog.add(new EntryEvent(vehicle1));

        Assert.assertTrue(ccsystem.previouslyRegistered(vehicle1, eventLog));
        Assert.assertFalse(ccsystem.previouslyRegistered(vehicle2, eventLog));
    }


    

}