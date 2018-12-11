package com.trafficmon;

import java.util.List;

public class CrossingEventBuilder {
    private Vehicle vehicle = Vehicle.withRegistration("A123 XYZ");
    private String comeInTime = "06:00:00";
    private String comeOutTime = "18:00:00";
    private List<ZoneBoundaryCrossing> eventLog = null;

    private CrossingEventBuilder() {
    }

    public static CrossingEventBuilder crossingEvent() {
        return new CrossingEventBuilder();
    }

    public CrossingEventBuilder setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        return this;
    }

    public CrossingEventBuilder setComeInTime(String comeInTime) {
        this.comeInTime = comeInTime;
        return this;
    }

    public CrossingEventBuilder setComeOutTime(String comeOutTime) {
        this.comeOutTime = comeOutTime;
        return this;
    }

    public CrossingEventBuilder setEventLog(List<ZoneBoundaryCrossing> eventLog) {
        this.eventLog = eventLog;
        return this;
    }

    public CrossingEvent build() {
        return new CrossingEvent(vehicle, comeInTime, comeOutTime, eventLog);
    }
}