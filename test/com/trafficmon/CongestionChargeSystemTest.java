package com.trafficmon;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.joda.time.*;
import java.util.Date;



import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class CongestionChargeSystemTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    public CongestionChargeSystem ccsystem = new CongestionChargeSystem();

    @Test
    public void vehicleEnteringZone() {

        ccsystem.vehicleEnteringZone(Vehicle.withRegistration("A123 XYZ"));
        ccsystem.vehicleLeavingZone(Vehicle.withRegistration("A123 XYZ"));
       // DateTime dateTime = new DateTime(timeMills);
        //System.out.println(System.currentTimeMillis());
        DateTime c = new DateTime(DateTimeUtils.currentTimeMillis());
        System.out.println(c);

        assertThat(ccsystem.getEventLog().size(), is(2));
        System.out.println(ccsystem.getEventLog());

    }
}