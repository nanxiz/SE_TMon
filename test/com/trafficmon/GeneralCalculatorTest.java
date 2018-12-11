package com.trafficmon;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;


public class GeneralCalculatorTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    private SystemServiceInterface systemService = context.mock(SystemServiceInterface.class);
    private GeneralCalculator calculator = new NewRuleCalculator();
    private final Vehicle vehicle = Vehicle.withRegistration("A123 XYZ");



    @Test
    public void testMinutesBetween() {
        assertEquals(calculator.minutesBetween(21555,99999),2);
        assertEquals(calculator.minutesBetween(398756,897654),9);
        assertEquals(calculator.minutesBetween(78999567,99998648),350);
    }

    @Test
    /**
     *If the new event is before the last event
     *Then checkOderingOf should return false
     */
    public void testNewEventBeforeLastError(){
        List<ZoneBoundaryCrossing> eventLog=new ArrayList<ZoneBoundaryCrossing>();
        new CrossingEvent().makeCrossingEvent(vehicle,"09:00:29","07:00:22",eventLog);
        assertFalse(calculator.checkOrderingOf(eventLog,vehicle));
    }

    @Test
    /**
     *if an entry is followed by an entry
     *then it should return false
     */
    public void testEntryBeforeEntryError(){
        List<ZoneBoundaryCrossing> eventLog=new ArrayList<ZoneBoundaryCrossing>();
        eventLog.add(new EntryEvent(vehicle));
        eventLog.add(new EntryEvent(vehicle));
        eventLog.add(new ExitEvent(vehicle));

        assertFalse(calculator.checkOrderingOf(eventLog,vehicle));
    }

    @Test
    /**
     *if an exit is followed by an exit
     *then CheckOderingOf should return false
     */
    public void testExitAfterExitError(){
        List<ZoneBoundaryCrossing> eventLog=new ArrayList<ZoneBoundaryCrossing>();
        eventLog.add(new EntryEvent(vehicle));
        eventLog.add(new ExitEvent(vehicle));
        eventLog.add(new ExitEvent(vehicle));
        assertFalse(calculator.checkOrderingOf(eventLog,vehicle));
    }

    @Test
    /**
     *if the last event is before the new event
     *an Entry/Exit Event followed by Exit/Entry Event
     *then checkOderingOf should return true
     */
    public void checkOrderingOf() {
        List<ZoneBoundaryCrossing> eventLog=new ArrayList<ZoneBoundaryCrossing>();
        new CrossingEvent().makeCrossingEvent(vehicle,"03:20:29","08:05:32",eventLog);
        new CrossingEvent().makeCrossingEvent(vehicle,"09:33:57","10:35:17",eventLog);

        assertTrue(calculator.checkOrderingOf(eventLog,vehicle));

    }

    @Test
    /**
     *If the vehicle's eventLog pass the checkOdering test
     *Then SystemService should be called to issue charge deduction
     */
    public void checkSystemServiceChargeDeduction() {
        List<ZoneBoundaryCrossing> eventLog=new ArrayList<ZoneBoundaryCrossing>();
        //create a vehicle's eventLog that could return true if checkOrderingOf(eventLog)
        new CrossingEvent().makeCrossingEvent(vehicle,"05:00:29","07:00:22",eventLog);
        new CrossingEvent().makeCrossingEvent(vehicle,"10:00:29","11:00:22",eventLog);
        new CrossingEvent().makeCrossingEvent(vehicle,"13:00:29","18:00:22",eventLog);

        context.checking(new Expectations(){{
            //the false below mean false to "!checkOrderingOf"
            // meaning the it is ok to charge the vehicle for its eventLog
            if (false) {
                exactly(1).of(systemService).chargeDetuction(vehicle, new BigDecimal(12));
            }
            else{}
        }});
        calculator.calculateOneVehicleCharge(vehicle,eventLog);
    }

    @Test
    /**
     * If the vehicle's eventLog fail the checkOdering test
     * Then SystemService should be called to trigger investigation to the vehicle
     */
    public void checkSystemServiceTriggerInvestigation(){
        List<ZoneBoundaryCrossing> eventLog=new ArrayList<ZoneBoundaryCrossing>();
        //create a vehicle's eventLog that could return false if checkOrderingOf(eventLog)
        new CrossingEvent().makeCrossingEvent(vehicle,"07:00:29","05:00:22",eventLog);
        context.checking(new Expectations(){{
            //the true below mean false to "!checkOrderingOf"
            // meaning charging the vehicle for its eventLog is problematic
            if (true) {
            }
            else{exactly(1).of(systemService).triggerInvestigation(vehicle);
            }
        }});
        calculator.calculateOneVehicleCharge(vehicle,eventLog);

    }
}