package com.trafficmon;

import org.junit.Test;
import static org.junit.Assert.*;


public class VehicleTest {

    /**
     * test if the toString method of Vehicle could convert
     * registered vehicle to string in right format
     */
    @Test
    public void testToString(){

        Vehicle vehicle = Vehicle.withRegistration("A123 XYZ");
        assertTrue(vehicle.toString().equals("Vehicle [A123 XYZ]"));
    }

    /**
     * test if the equals method in Vehicle can
     * detect whether 2 vehicle are the same(whether they have same registration)
     */
    @Test
    public void testEquals(){
        Vehicle vehicle1 = Vehicle.withRegistration("A123 XYZ");
        Vehicle vehicle2 = Vehicle.withRegistration("J091 4PY");
        Vehicle vehicle3 = Vehicle.withRegistration("A123 XYZ");

        assertTrue(vehicle1.equals(vehicle3));
        assertFalse(vehicle2.equals(vehicle3));

    }

    /**
     * check if the hashCode method in vehicle
     * only hashcode vehicle successfully registered
     * return 0 if vehicle not registered
     * different registered vehicle let hashcode return different value
     */
    @Test
    public void HashCodeTest(){
        Vehicle vehicle1 = Vehicle.withRegistration("A123 XYZ");
        Vehicle vehicle2 = Vehicle.withRegistration("A123 XYZ");
        Vehicle vehicle3 = Vehicle.withRegistration(null);
        Vehicle vehicle4 = Vehicle.withRegistration("J091 4PY");

        assertFalse(vehicle1.hashCode()== 0);
        assertTrue(vehicle3.hashCode()==0);
        assertTrue(vehicle1.hashCode()==vehicle2.hashCode());
        assertFalse(vehicle1.hashCode()==vehicle4.hashCode());

    }

}