package com.trafficmon;


//import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.*;

public class OldRuleCalculatorTest {
    private OldRuleCalculator oldCalculator = new OldRuleCalculator();
    private Vehicle vehicle=Vehicle.withRegistration("A123 XYZ");


    @Test
    /**
     *In this case vehicle stay in zone for 150 minutes
     * single stay
     */
    public void singleEntry(){
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        new CrossingEvent().makeCrossingEvent(vehicle,"12:08:30","14:38:30",eventLog);
        assertThat(oldCalculator.calculateCharge(eventLog),is(BigDecimal.valueOf(150).multiply(oldCalculator.CHARGE_RATE_POUNDS_PER_MINUTE)));
    }

    @Test
    /**
     *In this case vehicle stay in zone for in total 270 minutes
     * Multiple stays
     */
    public void MultipleEntry(){
        List<ZoneBoundaryCrossing> eventLog =new ArrayList<ZoneBoundaryCrossing>();
        new CrossingEvent().makeCrossingEvent(vehicle,"12:08:30","14:38:30",eventLog);
        new CrossingEvent().makeCrossingEvent(vehicle,"17:24:18","19:24:18",eventLog);
        new CrossingEvent().makeCrossingEvent(vehicle,"20:11:57","21:21:57",eventLog);

        assertThat(oldCalculator.calculateCharge(eventLog),is(BigDecimal.valueOf(340).multiply(oldCalculator.CHARGE_RATE_POUNDS_PER_MINUTE)));
    }

}