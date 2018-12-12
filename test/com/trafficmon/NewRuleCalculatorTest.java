package com.trafficmon;

import org.junit.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static com.trafficmon.CrossingEventBuilder.crossingEvent;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;




public class NewRuleCalculatorTest {

    private NewRuleCalculator newCalculator = new NewRuleCalculator();

    /**
     * test whether the checkIfTimeBeforeTwo method working
     * It should return 4 when time is before 14:00:00
     * and return 6 when time is after 14:00:00
     */
    @Test
    public void checkMoneyIssuedBasedOnEnterTime(){
        assertEquals(6,newCalculator.checkIfTimeBeforeTwo(crossingEvent().build().dateTimeToMilis("03:22:56")));
        assertEquals(6,newCalculator.checkIfTimeBeforeTwo(crossingEvent().build().dateTimeToMilis("11:29:33")));
        assertEquals(4,newCalculator.checkIfTimeBeforeTwo(crossingEvent().build().dateTimeToMilis("18:02:24")));
        assertEquals(4,newCalculator.checkIfTimeBeforeTwo(crossingEvent().build().dateTimeToMilis("23:22:06")));
    }


    /**
     * if vehicle entries 1 times before 14:00:00
     * total stay time < 4 hour
     */
    @Test
    public void singleEntryBefore2pmStayWithin4Hours(){
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        crossingEvent().setComeInTime("12:08:30").setComeOutTime("15:32:59").setEventLog(eventLog).build().addEventLog();
        assertThat(newCalculator.calculateCharge(eventLog),is(BigDecimal.valueOf(6)));
    }


    /**
     * if vehicle entries 1 times after 14:00:00
     * total stay time < 4 hour
     */
    @Test
    public void singleEntryAfter2pmStayWithin4Hours(){
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        crossingEvent().setComeInTime("16:18:20").setComeOutTime("17:39:59").setEventLog(eventLog).build().addEventLog();
        assertThat(newCalculator.calculateCharge(eventLog),is(BigDecimal.valueOf(4)));
    }


    /**
     * if vehicle entries 1 times after 14:00:00
     * total stay time = 4 hour
     */
    @Test
    public void singleEntryAfter2pmStayEqual4Hours(){
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        crossingEvent().setComeInTime("16:18:20").setComeOutTime("20:18:20").setEventLog(eventLog).build().addEventLog();
        assertThat(newCalculator.calculateCharge(eventLog),is(BigDecimal.valueOf(4)));
    }


    /**
     * if vehicle entries 1 times
     * total stay time > 4 hour
     */
    @Test
    public void singleEntryAfter2pmStayOver4Hours(){
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        crossingEvent().setComeInTime("16:18:20").setComeOutTime("21:08:50").setEventLog(eventLog).build().addEventLog();
        assertThat(newCalculator.calculateCharge(eventLog),is(BigDecimal.valueOf(12)));
    }


    /**
     * if vehicle entries multiple(>2) times,
     * always leave and come back < 4 hours,
     * total stay time < 4 hour,
     * The vehicle should only be charged one time for the first entry
     */
    @Test
    public void multipleEntryGapWithin4HourOnlyChargeOnce(){
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        crossingEvent().setComeInTime("10:03:30").setComeOutTime("10:32:29").setEventLog(eventLog).build().addEventLog();
        crossingEvent().setComeInTime("13:03:30").setComeOutTime("14:12:29").setEventLog(eventLog).build().addEventLog();
        crossingEvent().setComeInTime("15:20:52").setComeOutTime("15:33:17").setEventLog(eventLog).build().addEventLog();
        assertThat(newCalculator.calculateCharge(eventLog),is(BigDecimal.valueOf(6)));

    }


    /**
     * if vehicle entries multiple(>2) time
     * always leave and come back < 4 hours
     * total stay time > 4 hour
     * he vehicle should be charged exact 12
     */
    @Test
    public void multipleEntryIn2TimeZoneGapUnder4HourTotalTimeOver4Hours(){
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        crossingEvent().setComeInTime("08:03:30").setComeOutTime("10:12:29").setEventLog(eventLog).build().addEventLog();
        crossingEvent().setComeInTime("11:20:52").setComeOutTime("15:33:17").setEventLog(eventLog).build().addEventLog();
        crossingEvent().setComeInTime("17:29:48").setComeOutTime("18:09:30").setEventLog(eventLog).build().addEventLog();
        assertThat(newCalculator.calculateCharge(eventLog),is(BigDecimal.valueOf(12)));

    }


    /**
     * if vehicle entries multiple(>2) time
     * always leave and come back > 4 hours
     * total stay time < 4 hour
     * The vehicle should be charged: number_of_enter_before_2pm * 6 + number_of_enter_after_2pm * 4
     * in this case, it should be charged 20
     */
    @Test
    public void multipleEntryIn2TimeZoneGapOver4HourInTotalMoreThan4Hours(){
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        crossingEvent().setComeInTime("02:03:30").setComeOutTime("02:12:29").setEventLog(eventLog).build().addEventLog();
        crossingEvent().setComeInTime("06:20:52").setComeOutTime("06:33:17").setEventLog(eventLog).build().addEventLog();
        crossingEvent().setComeInTime("14:29:48").setComeOutTime("15:09:30").setEventLog(eventLog).build().addEventLog();
        crossingEvent().setComeInTime("20:55:48").setComeOutTime("21:09:30").setEventLog(eventLog).build().addEventLog();
        assertThat(newCalculator.calculateCharge(eventLog),is(BigDecimal.valueOf(20)));

    }





}