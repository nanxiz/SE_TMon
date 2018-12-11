package com.trafficmon;

public class tt {
    //private PenaltiesService penaltiesService = OperationsTeam.getInstance();

    public static void main(String[] args) throws Exception {
        PenaltiesService penaltiesService = OperationsTeam.getInstance();
        CongestionChargeSystem congestionChargeSystem = new CongestionChargeSystem();
    congestionChargeSystem.vehicleEnteringZone(Vehicle.withRegistration("A123 XYZ"));
    delaySeconds(15);
    System.out.println(congestionChargeSystem.getEventLog());

    congestionChargeSystem.vehicleEnteringZone(Vehicle.withRegistration("J091 4PY"));
    delayMinutes(11);

    congestionChargeSystem.vehicleLeavingZone(Vehicle.withRegistration("A123 XYZ"));
    delayMinutes(1);

    congestionChargeSystem.vehicleLeavingZone(Vehicle.withRegistration("J091 4PY"));
    congestionChargeSystem.calculateCharges();
}
    private static void delayMinutes(int mins) throws InterruptedException {
        delaySeconds(mins * 2);
    }
    private static void delaySeconds(int secs) throws InterruptedException {
        Thread.sleep(secs * 1);
    } }
