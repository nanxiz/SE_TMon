package com.trafficmon;

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
    //private SystemService systemServices = Mokito.mock;
    //private PrintStream printOut = context.mock(PrintStream.class);
    private PenaltiesService penaltiesService = context.mock(PenaltiesService.class);
    private AccountsService accountsService = context.mock(AccountsService.class);
    private CongestionChargeSystem ccsystem = new CongestionChargeSystem(penaltiesService);
    //private VehiclesCrossingsRecord vehiclesCrossingsRecord = new VehiclesCrossingsRecord();
    private SystemServiceInterface systemService = context.mock(SystemServiceInterface.class);
    //SystemServiceInterface systemServices = new SystemService();
    private VehiclesCrossingsRecord vehiclesCrossingsRecord = context.mock(VehiclesCrossingsRecord.class);
    private final Vehicle vehicle1 = Vehicle.withRegistration("A123 XYZ");
    private final Vehicle vehicle2 = Vehicle.withRegistration("J091 4PY");

    //private Vehicle vehicle;




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



    //@Test(expected = Test.None.class)
    /*
     * Will deduct charge from the account without exceptions
     */
   /*
    public void checkDeductingChargeFromAccount() throws AccountNotRegisteredException,InsufficientCreditException{
        BigDecimal charge = new BigDecimal(6);
        systemServices.chargeDetuction(vehicle1,charge);

        /*
        System.setOut(printOut);
        context.checking(new Expectations(){{
            oneOf(printOut).println(startsWith("Chharge made to"));
        }});
        ccsystem.attemptToDeductCharges(vehicle1,charge);
       // try{
       // systemServices.chargeDetuction(vehicle1,charge);}
       // catch (AccountNotRegisteredException|InsufficientCreditException e){}
       */
  //  }

    @Test
    public void willLetVehicleRecordCalculate(){
        context.checking(new Expectations(){{
            exactly(1).of(vehiclesCrossingsRecord).fileEventLogIntoVehiclesRecord(ccsystem.getEventLog());
        }});
        ccsystem.vehicleLeavingZone(vehicle1);
        ccsystem.vehicleLeavingZone(vehicle2);
    }

    @Test
    /**
     * Will deduct charge from the account without exceptions
     */
    public void checkDeductingChargeFromAccount(){
        BigDecimal charge = new BigDecimal(1);

        context.checking(new Expectations(){{

           // exactly(1).of(vehiclesCrossingsRecord).fileEventLogIntoVehiclesRecord(eventLog);
           // oneOf(systemServices).chargeDetuction(vehicle1,charge); will(thr);
        }});

        ccsystem.calculateCharges();
    }

    @Test//(expected =InsufficientCreditException.class)
    /**
     * Will issue penalty notice when the credit is insufficient
     */
    public void checkInsufficientCreditException(){//throws AccountNotRegisteredException,InsufficientCreditException{

        BigDecimal charge = new BigDecimal(10000000);
        //systemServices.chargeDetuction(vehicle1,charge);
        context.checking(new Expectations(){{
            exactly(1).of(penaltiesService).issuePenaltyNotice(vehicle1, charge);
            //exactly(1).of(systemService).issuePenalty(vehicle1, charge);

                         }}
        );
        //ccsystem.attemptToDeductCharges(vehicle1,charge);
        //ccsystem.attemptToDeductCharges(vehicle1,charge);
        //systemServices.issuePenalty(vehicle1,charge);
    }




    @Test
    /**
     * Will issue penalty notice when the vehicle is not previously registered
     */
    public void checkPenaltyNoticeForNotPreviouslyRegistered(){
        Vehicle vehicle = Vehicle.withRegistration("AAA XXX");//this vehicle is not yet registered
        BigDecimal charge = new BigDecimal(1);

        context.checking(new Expectations(){{
            exactly(1).of(penaltiesService).issuePenaltyNotice(vehicle, charge);
        }});

        //systemServices.systemBehaviors(vehicle, charge);

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
     * vehicle1 in here is registered while vehicle2 is not registered
     */
    public void checkIfPreviouslyRegistered(){
        List<ZoneBoundaryCrossing> eventLog=new ArrayList<>();
        eventLog.add(new EntryEvent(vehicle1));

        Assert.assertTrue(ccsystem.previouslyRegistered(vehicle1, eventLog));
        Assert.assertFalse(ccsystem.previouslyRegistered(vehicle2, eventLog));
    }


    

}