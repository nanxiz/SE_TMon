package com.trafficmon;

import org.junit.jupiter.api.Test;
import org.junit.Rule;
import java.math.*;
import java.util.ArrayList;

import static org.hamcrest.core.Is.is; //need to be changed
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CongestionChargeSystemTest {

    @Rule
    public CongestionChargeSystem ccsystem = new CongestionChargeSystem();

    @Test
    public void vehicleEnteringZone() {
        ccsystem.vehicleEnteringZone(Vehicle.withRegistration("A123 XYZ"));
        ccsystem.vehicleLeavingZone(Vehicle.withRegistration("A123 XYZ"));
        assertThat(ccsystem.getEventLog().size(), is(2));

    }
}