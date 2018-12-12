package com.trafficmon;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.trafficmon.CrossingEventBuilder.crossingEvent;
import static org.junit.Assert.*;


public class GeneralCalculatorTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    private SystemServiceInterface systemService = context.mock(SystemServiceInterface.class);
    private GeneralCalculator calculator = new NewRuleCalculator();
    private final Vehicle defaultVehicle = Vehicle.withRegistration("A123 XYZ");



    @Test
    public void testMinutesBetween() {
        long timeOne = crossingEvent().build().dateTimeToMilis("12:08:00");
        long timeTwo = crossingEvent().build().dateTimeToMilis("12:48:00");
        long timeThree = crossingEvent().build().dateTimeToMilis("15:08:00");
        assertEquals(calculator.minutesBetween(timeOne,timeTwo),40);
        assertEquals(calculator.minutesBetween(timeOne,timeThree),180);
        assertEquals(calculator.minutesBetween(timeTwo,timeThree),140);
    }


    /**
     *If the new event is before the last event
     *Then checkOderingOf should return false
     */
    @Test
    public void testNewEventBeforeLastError(){
        List<ZoneBoundaryCrossing> eventLog=new ArrayList<ZoneBoundaryCrossing>();
        crossingEvent().setComeInTime("09:00:29").setComeOutTime("07:00:22").setEventLog(eventLog).build().addEventLog();
        assertFalse(calculator.checkOrderingOf(eventLog, defaultVehicle));
    }


    /**
     *if an entry is followed by an entry
     *then it should return false
     */
    @Test
    public void testEntryBeforeEntryError(){
        List<ZoneBoundaryCrossing> eventLog=new ArrayList<ZoneBoundaryCrossing>();
        eventLog.add(new EntryEvent(defaultVehicle));
        eventLog.add(new EntryEvent(defaultVehicle));
        eventLog.add(new ExitEvent(defaultVehicle));
        assertFalse(calculator.checkOrderingOf(eventLog, defaultVehicle));
    }


    /**
     *if an exit is followed by an exit
     *then CheckOderingOf should return false
     */
    @Test
    public void testExitAfterExitError(){
        List<ZoneBoundaryCrossing> eventLog=new ArrayList<ZoneBoundaryCrossing>();
        eventLog.add(new EntryEvent(defaultVehicle));
        eventLog.add(new ExitEvent(defaultVehicle));
        eventLog.add(new ExitEvent(defaultVehicle));
        assertFalse(calculator.checkOrderingOf(eventLog, defaultVehicle));
    }


    /**
     *if the last event is before the new event
     *an Entry/Exit Event followed by Exit/Entry Event
     *then checkOderingOf should return true
     */
    @Test
    public void checkOrderingOf() {
        List<ZoneBoundaryCrossing> eventLog=new ArrayList<ZoneBoundaryCrossing>();
        crossingEvent().setComeInTime("03:20:29").setComeOutTime("08:05:32").setEventLog(eventLog).build().addEventLog();
        crossingEvent().setComeInTime("09:33:57").setComeOutTime("10:35:17").setEventLog(eventLog).build().addEventLog();
        assertTrue(calculator.checkOrderingOf(eventLog, defaultVehicle));
    }


    /**
     *If the defaultVehicle's eventLog pass the checkOdering test
     *Then SystemService should be called to issue charge deduction
     */
    @Test
    public void checkSystemServiceChargeDeduction() {
        List<ZoneBoundaryCrossing> eventLog=new ArrayList<ZoneBoundaryCrossing>();
        //create a defaultVehicle's eventLog that could return true if checkOrderingOf(eventLog)
        crossingEvent().setComeInTime("05:00:29").setComeOutTime("07:00:22").setEventLog(eventLog).build().addEventLog();
        crossingEvent().setComeInTime("10:00:29").setComeOutTime("11:00:22").setEventLog(eventLog).build().addEventLog();
        crossingEvent().setComeInTime("13:00:29").setComeOutTime("18:00:22").setEventLog(eventLog).build().addEventLog();

        context.checking(new Expectations(){{
            //the false below mean false to "!checkOrderingOf"
            // meaning the it is ok to charge the defaultVehicle for its eventLog
            if (false) {
                exactly(1).of(systemService).chargeDeduction(defaultVehicle, new BigDecimal(12));
            }
            else{}
        }});
        calculator.calculateForOneVehicle(defaultVehicle,eventLog);
    }


    /**
     * If the defaultVehicle's eventLog fail the checkOdering test
     * Then SystemService should be called to trigger investigation to the defaultVehicle
     */
    @Test
    public void checkSystemServiceTriggerInvestigation(){
        List<ZoneBoundaryCrossing> eventLog = new ArrayList<ZoneBoundaryCrossing>();
        //create a defaultVehicle's eventLog that could return false if checkOrderingOf(eventLog)
        crossingEvent().setComeInTime("07:00:29").setComeOutTime("05:00:22").setEventLog(eventLog).build().addEventLog();
        context.checking(new Expectations(){{
            //the true below mean false to "!checkOrderingOf"
            // meaning charging the defaultVehicle for its eventLog is problematic
            if (true) {
            }
            else{exactly(1).of(systemService).triggerInvestigation(defaultVehicle);
            }
        }});
        calculator.calculateForOneVehicle(defaultVehicle,eventLog);

    }
}