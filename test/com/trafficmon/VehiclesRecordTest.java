package com.trafficmon;


import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VehiclesRecordTest {

    private Vehicle vehicle;
    private VehiclesRecord vehiclesRecord = new VehiclesRecord();
    private List<ZoneBoundaryCrossing> crossings = new ArrayList<ZoneBoundaryCrossing>();
    private final EntryEvent entry = new EntryEvent(Vehicle.withRegistration("A123 XYZ"));
    private final ExitEvent exit = new ExitEvent(Vehicle.withRegistration("A123 XYZ"));
    private final EntryEvent entry2 = new EntryEvent(Vehicle.withRegistration("A123 XYZ"));
    private final ExitEvent exit2 = new ExitEvent(Vehicle.withRegistration("A123 XYZ"));

    @Test
    /**
     check
     if the exit time before the entry time
     then it should return false
     */
    public void testCrossingBeforeLastEventError(){
        crossings.add(entry);
        crossings.add(exit);
        ZoneBoundaryCrossing lastEvent = crossings.get(0);
        ZoneBoundaryCrossing crossing = crossings.get(1);
        lastEvent.setTimestamp(10000);
        crossing.setTimestamp(5000);
        assertFalse(vehiclesRecord.checkOrderingOf(crossings,vehicle));
    }


    @Test
    /**
     check
     if an entry is followed by an entry
     then it should return false
     */
    public void testEntryBeforeEntryError(){
        crossings.add(entry);
        crossings.add(entry2);
        assertFalse(vehiclesRecord.checkOrderingOf(crossings,vehicle));
    }


    @Test
    /**
     check
     if an exit is followed by an exit
     then is should return false
     */
    public void testExitBeforeExitError(){
        crossings.add(exit);
        crossings.add(exit2);
        assertFalse(vehiclesRecord.checkOrderingOf(crossings,vehicle));
    }


    @Test
    /**
     check
     if an entry is followed by an exit
     then it should return true
     */
    public void testCrossingAfterLastEvent(){
        crossings.add(entry);
        crossings.add(exit);
        ZoneBoundaryCrossing lastEvent = crossings.get(0);
        ZoneBoundaryCrossing crossing = crossings.get(1);
        lastEvent.setTimestamp(5000);
        crossing.setTimestamp(10000);
        assertTrue(vehiclesRecord.checkOrderingOf(crossings,vehicle));
    }
}
