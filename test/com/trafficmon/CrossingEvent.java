package com.trafficmon;

import org.joda.time.DateTimeUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;


public class CrossingEvent {
    private  String timeIn;
    private Vehicle vehicle;
    private  String timeOut;
    private  List<ZoneBoundaryCrossing> eventLog;
    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm:ss");

    public CrossingEvent(Vehicle vehicle, String comeInTime, String comeOutTime, List<ZoneBoundaryCrossing> eventLog){
        this.timeIn = comeInTime;
        this.timeOut = comeOutTime;
        this.eventLog = eventLog;
        this.vehicle = vehicle;
    }

    public long dateTimeToMilis(String dateTime){
        return dtf.parseDateTime(dateTime).getMillis();
    }

    public void addEventLog(){
        DateTimeUtils.setCurrentMillisFixed(dtf.parseDateTime(timeIn).getMillis());
        eventLog.add((new EntryEvent(vehicle)));
        DateTimeUtils.setCurrentMillisFixed(dtf.parseDateTime(timeOut).getMillis());
        eventLog.add ((new ExitEvent(vehicle)));
    }


}

