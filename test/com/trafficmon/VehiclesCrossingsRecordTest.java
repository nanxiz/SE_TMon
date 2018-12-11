package com.trafficmon;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VehiclesCrossingsRecordTest {

    private Vehicle vehicle;
    private VehiclesCrossingsRecord vehiclesCrossingsRecord = new VehiclesCrossingsRecord();
    private List<ZoneBoundaryCrossing> crossings = new ArrayList<ZoneBoundaryCrossing>();
    private GeneralCalculator calculator = new NewRuleCalculator();
    private final EntryEvent entry = new EntryEvent(Vehicle.withRegistration("A123 XYZ"));
    private final ExitEvent exit = new ExitEvent(Vehicle.withRegistration("A123 XYZ"));
    private final EntryEvent entry2 = new EntryEvent(Vehicle.withRegistration("A123 XYZ"));
    private final ExitEvent exit2 = new ExitEvent(Vehicle.withRegistration("A123 XYZ"));

//补完







}
