package com.trafficmon;


import org.junit.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static com.trafficmon.CrossingEventBuilder.crossingEvent;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class OldRuleCalculatorTest {
    private OldRuleCalculator oldCalculator = new OldRuleCalculator();


    /**
     *In this case vehicle stay in zone for 150 minutes
     * single stay
     */
    @Test
    public void singleEntry(){
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        crossingEvent().setComeInTime("12:08:30").setComeOutTime("14:38:30").setEventLog(eventLog).build().addEventLog();
        assertThat(oldCalculator.calculateCharge(eventLog),is(BigDecimal.valueOf(150).multiply(oldCalculator.CHARGE_RATE_POUNDS_PER_MINUTE)));
    }


    /**
     *In this extreme case vehicle enter and enxit at the same time
     * single stay
     */
    @Test
    public void sameTimeEntryAndLeave(){
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        crossingEvent().setComeInTime("12:08:30").setComeOutTime("12:08:30").setEventLog(eventLog).build().addEventLog();
        assertThat(oldCalculator.calculateCharge(eventLog),is(BigDecimal.valueOf(0).multiply(oldCalculator.CHARGE_RATE_POUNDS_PER_MINUTE)));
    }



    /**
     *In this case vehicle stay in zone for in total 270 minutes
     * Multiple stays
     */
    @Test
    public void MultipleEntry(){
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        crossingEvent().setComeInTime("12:08:30").setComeOutTime("14:38:30").setEventLog(eventLog).build().addEventLog();
        crossingEvent().setComeInTime("17:24:18").setComeOutTime("19:24:18").setEventLog(eventLog).build().addEventLog();
        crossingEvent().setComeInTime("20:11:57").setComeOutTime("21:21:57").setEventLog(eventLog).build().addEventLog();

        assertThat(oldCalculator.calculateCharge(eventLog),is(BigDecimal.valueOf(340).multiply(oldCalculator.CHARGE_RATE_POUNDS_PER_MINUTE)));
    }

}