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


    

}