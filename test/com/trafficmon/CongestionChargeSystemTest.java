package com.trafficmon;

import static org.junit.Assert.*;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Assert;
import org.junit.Rule;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CongestionChargeSystemTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private PenaltiesService penaltiesService = context.mock(PenaltiesService.class);

    private CongestionChargeSystem ccsystem = new CongestionChargeSystem(penaltiesService);
    private VehiclesRecord vehiclesRecord = new VehiclesRecord();

    private final Vehicle vehicle1 = Vehicle.withRegistration("A123 XYZ");
    private final Vehicle vehicle2 = Vehicle.withRegistration("J091 4PY");

    private Vehicle vehicle;




    @Test
    public void checkEventLogSize() {
        ccsystem.vehicleEnteringZone(vehicle1);
        ccsystem.vehicleLeavingZone(vehicle1);
        Assert.assertEquals(ccsystem.getEventLog().size(), 2);
    }

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

    @Test
    /**
     * Will deduct charge from the account without exceptions
     */
    public void checkDeductingChargeFromAccount(){
        BigDecimal charge = new BigDecimal(1);

        context.checking(new Expectations(){{
            exactly(0).of(penaltiesService).issuePenaltyNotice(vehicle1 ,charge);
        }});

        ccsystem.operating(vehicle1, charge);
    }

    @Test
    /**
     * Will issue penalty notice when the credit is insufficient
     */
    public void checkPenaltyNoticeForInsufficientCredit(){
        BigDecimal charge = new BigDecimal(10000000);

        context.checking(new Expectations(){{
            exactly(1).of(penaltiesService).issuePenaltyNotice(vehicle1, charge);
        }});

        ccsystem.operating(vehicle1, charge);

    }

    @Test
    /**
     * Will issue penalty notice when the vehicle is not previously registered
     */
    public void checkPenaltyNoticeForNotPreviouslyRegistered(){
        Vehicle vehicle = Vehicle.withRegistration("AAA XXX");
        BigDecimal charge = new BigDecimal(1);

        context.checking(new Expectations(){{
            exactly(1).of(penaltiesService).issuePenaltyNotice(vehicle, charge);
        }});

        ccsystem.operating(vehicle, charge);

    }


    @Test
    /**
     * Will issue penalty notice for disordering events
     */
    public void checkInvestigation(){
        ccsystem.vehicleEnteringZone(vehicle1);
        ccsystem.vehicleEnteringZone(vehicle1);

        context.checking(new Expectations(){{
            exactly(1).of(penaltiesService).triggerInvestigationInto(vehicle1);
        }});

        ccsystem.calculateCharges();
    }


    @Test
    /**
     * Check if the entering vehicle registers correctly
     */
    public void checkIfPreviouslyRegistered(){
        List<ZoneBoundaryCrossing> eventLog=new ArrayList<>();
        eventLog.add(new EntryEvent(vehicle1));

        Assert.assertTrue(vehiclesRecord.previouslyRegistered(vehicle1, eventLog));
        Assert.assertFalse(vehiclesRecord.previouslyRegistered(vehicle2, eventLog));
    }


    @Test
    /**
     check
     if the exit time before the entry time
     then it should return false
     */
    public void testCrossingBeforeLastEventError(){
        List<ZoneBoundaryCrossing> crossings = new ArrayList<ZoneBoundaryCrossing>();
        EntryEvent entry = new EntryEvent(Vehicle.withRegistration("A123 XYZ"));
        ExitEvent exit = new ExitEvent(Vehicle.withRegistration("A123 XYZ"));
        VehiclesRecord checkOrdering = new VehiclesRecord();

        crossings.add(entry);
        crossings.add(exit);

        ZoneBoundaryCrossing lastEvent = crossings.get(0);
        ZoneBoundaryCrossing crossing = crossings.get(1);
        lastEvent.setTimestamp(10000);
        crossing.setTimestamp(5000);
        assertFalse(checkOrdering.checkOrderingOf(crossings,vehicle));
    }


    @Test
    /**
     check
     if an entry is followed by an entry
     then it should return false
     */
    public void testEntryBeforeEntryError(){
        List<ZoneBoundaryCrossing> crossings = new ArrayList<ZoneBoundaryCrossing>();
        EntryEvent entry1 = new EntryEvent(Vehicle.withRegistration("A123 XYZ"));
        EntryEvent entry2 = new EntryEvent(Vehicle.withRegistration("A123 XYZ"));
        VehiclesRecord checkOrdering = new VehiclesRecord();

        crossings.add(entry1);
        crossings.add(entry2);

        assertFalse(checkOrdering.checkOrderingOf(crossings,vehicle));
    }


    @Test
    /**
     check
     if an exit is followed by an exit
     then is should return false
     */
    public void testExitBeforeExitError(){
        List<ZoneBoundaryCrossing> crossings = new ArrayList<ZoneBoundaryCrossing>();
        ExitEvent exit1 = new ExitEvent(Vehicle.withRegistration("A123 XYZ"));
        ExitEvent exit2 = new ExitEvent(Vehicle.withRegistration("A123 XYZ"));
        VehiclesRecord checkOrdering = new VehiclesRecord();

        crossings.add(exit1);
        crossings.add(exit2);

        assertFalse(checkOrdering.checkOrderingOf(crossings,vehicle));
    }


    @Test
    /**
     check
     if an entry is followed by an exit
     then it should return true
     */
    public void testCrossingAfterLastEvent(){
        List<ZoneBoundaryCrossing> crossings = new ArrayList<ZoneBoundaryCrossing>();
        EntryEvent entry = new EntryEvent(Vehicle.withRegistration("A123 XYZ"));
        ExitEvent exit = new ExitEvent(Vehicle.withRegistration("A123 XYZ"));
        VehiclesRecord checkOrdering = new VehiclesRecord();

        crossings.add(entry);
        crossings.add(exit);

        ZoneBoundaryCrossing lastEvent = crossings.get(0);
        ZoneBoundaryCrossing crossing = crossings.get(1);
        lastEvent.setTimestamp(5000);
        crossing.setTimestamp(10000);
        assertTrue(checkOrdering.checkOrderingOf(crossings,vehicle));
    }

}