package com.trafficmon;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.core.Is.is;

import static org.hamcrest.MatcherAssert.assertThat;




public class NewRuleCalculatorTest {
    @Rule
    //public CrossingEvent aCrossingEvent = new CrossingEvent();
    public JUnitRuleMockery context = new JUnitRuleMockery();
    private PenaltiesService operationsTeam = context.mock(PenaltiesService.class);
    private Calculator newCalculator = new NewRuleCalculator();
    private Vehicle vehicle=Vehicle.withRegistration("A123 XYZ");

    private CongestionChargeSystem congestionChargeSystem = new CongestionChargeSystem(operationsTeam);
    //private List<ZoneBoundaryCrossing> eventLog = new ArrayList<>();


    @Test
    /**
     * if vehicle entries 1 times before 14:00:00
     * total stay time < 4 hour
     */
    public void singleEntryBefore2pmStayWithin4Hours(){
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        new CrossingEvent().makeCrossingEvent(vehicle,"10:08:30","12:32:59",eventLog);
        assertThat(newCalculator.calculateChargeForTimeInZone(eventLog),is(BigDecimal.valueOf(6)));
    }

    @Test
    /**
     * if vehicle entries 1 times after 14:00:00
     * total stay time < 4 hour
     */
    public void singleEntryAfter2pmStayWithin4Hours(){
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        new CrossingEvent().makeCrossingEvent(vehicle,"16:18:20","17:39:59",eventLog);
        assertThat(newCalculator.calculateChargeForTimeInZone(eventLog),is(BigDecimal.valueOf(4)));
    }

    @Test
    /**
     * if vehicle entries 1 times
     * total stay time = 4 hour
     */
    public void singleEntryAfter2pmStayEqual4Hours(){
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        new CrossingEvent().makeCrossingEvent(vehicle,"16:18:20","20:18:20",eventLog);
        assertThat(newCalculator.calculateChargeForTimeInZone(eventLog),is(BigDecimal.valueOf(4)));
    }

    @Test
    /**
     * if vehicle entries 1 times
     * total stay time > 4 hour
     */
    public void singleEntryAfter2pmStayOver4Hours(){
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        new CrossingEvent().makeCrossingEvent(vehicle,"16:18:20","21:08:50",eventLog);
        assertThat(newCalculator.calculateChargeForTimeInZone(eventLog),is(BigDecimal.valueOf(12)));
    }

    @Test
    /**
     * if vehicle entries multiple(>2) times,
     * always leave and come back < 4 hours,
     * total stay time < 4 hour,
     * The vehicle should only be charged one time for the first entry
     */
    public void multipleEntryGapWithin4HourOnlyChargeOnce(){
        //Vehicle vehicle=Vehicle.withRegistration("A123 XYZ");
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        new CrossingEvent().makeCrossingEvent(vehicle,"10:03:30","10:32:29",eventLog);
        new CrossingEvent().makeCrossingEvent(vehicle,"13:03:30","14:12:29",eventLog);
        new CrossingEvent().makeCrossingEvent(vehicle,"15:20:52","15:33:17",eventLog);
        //new CrossingEvent().makeCrossingEvent(vehicle,"17:29:48","20:09:30",eventLog);
        assertThat(newCalculator.calculateChargeForTimeInZone(eventLog),is(BigDecimal.valueOf(6)));

    }

 //   @Test
 //   /**
//     * if vehicle entries multiple(>2) times in one time zone(before or after 2)
 //    * always leave and come back < 4 hours
//     * total stay time > 4 hour
//     */
//    public void multipleEntryIn1TimeZoneGapUnder4HourTotalTimeOver4Hours(){
//        //Vehicle vehicle=Vehicle.withRegistration("A123 XYZ");
//        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
//        new CrossingEvent().makeCrossingEvent(vehicle,"04:03:30","06:12:29",eventLog);
//        new CrossingEvent().makeCrossingEvent(vehicle,"04:03:30","06:12:29",eventLog);
 //       new CrossingEvent().makeCrossingEvent(vehicle,"08:20:52","11:33:17",eventLog);
 //       //new CrossingEvent().makeCrossingEvent(vehicle,"17:29:48","20:09:30",eventLog);
 //       assertThat(newCalculator.calculateChargeForTimeInZone(eventLog),is(BigDecimal.valueOf(12)));

 //   }

    @Test
    /**
     * if vehicle entries multiple(>2) times distributed across 2 time zone
     * always leave and come back < 4 hours
     * total stay time > 4 hour
     * he vehicle should be charged exact 12
     */
    public void multipleEntryIn2TimeZoneGapUnder4HourTotalTimeOver4Hours(){
       // Vehicle vehicle=Vehicle.withRegistration("A123 XYZ");
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        new CrossingEvent().makeCrossingEvent(vehicle,"08:03:30","10:12:29",eventLog);
        new CrossingEvent().makeCrossingEvent(vehicle,"11:20:52","15:33:17",eventLog);
        new CrossingEvent().makeCrossingEvent(vehicle,"17:29:48","18:09:30",eventLog);
        assertThat(newCalculator.calculateChargeForTimeInZone(eventLog),is(BigDecimal.valueOf(12)));

    }

//    @Test
//    /**
//     * if vehicle entries multiple(>2) times in 1 time zone(before or after 14:00:00)
//     * always leave and come back > 4 hours
//     * total stay time < 4 hour
//     */
 //   public void multipleEntryIn1TimeZoneGapOver4HourInTotalMoreThan4Hours(){
//        //Vehicle vehicle=Vehicle.withRegistration("A123 XYZ");
//        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
//        new CrossingEvent().makeCrossingEvent(vehicle,"02:03:30","02:12:29",eventLog);
//        new CrossingEvent().makeCrossingEvent(vehicle,"06:20:52","06:33:17",eventLog);
 //       new CrossingEvent().makeCrossingEvent(vehicle,"11:29:48","12:09:30",eventLog);
//        assertThat(newCalculator.calculateChargeForTimeInZone(eventLog),is(BigDecimal.valueOf(18)));///??????

//    }

    @Test
    /**
     * if vehicle entries multiple(>2) times distributed in 2 time zone
     * always leave and come back > 4 hours
     * total stay time < 4 hour
     * The vehicle should be charged: number_of_enter_before_2 * 6 + number_of_enter_after_2 * 4
     * in this case, it should be charged 20
     */
    public void multipleEntryIn2TimeZoneGapOver4HourInTotalMoreThan4Hours(){
        //Vehicle vehicle=Vehicle.withRegistration("A123 XYZ");
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        new CrossingEvent().makeCrossingEvent(vehicle,"02:03:30","02:12:29",eventLog);
        new CrossingEvent().makeCrossingEvent(vehicle,"06:20:52","06:33:17",eventLog);
        new CrossingEvent().makeCrossingEvent(vehicle,"14:29:48","15:09:30",eventLog);
        new CrossingEvent().makeCrossingEvent(vehicle,"20:55:48","21:09:30",eventLog);

        assertThat(newCalculator.calculateChargeForTimeInZone(eventLog),is(BigDecimal.valueOf(20)));

    }





}