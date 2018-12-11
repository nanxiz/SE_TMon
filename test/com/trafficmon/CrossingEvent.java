package com.trafficmon;

import org.joda.time.DateTimeUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class CrossingEvent {
    //private final DateTime timeIn;
    //private final DateTime timeOut;
    private final List<ZoneBoundaryCrossing> randomEventLog = new ArrayList<ZoneBoundaryCrossing>();

    private DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm:ss");

    public void makeCrossingEvent(Vehicle vehicle, String comeInTime, String comeOutTime, List<ZoneBoundaryCrossing> eventLog){
        DateTimeUtils.setCurrentMillisFixed(dtf.parseDateTime(comeInTime).getMillis());
        eventLog.add((new EntryEvent(vehicle)));
        DateTimeUtils.setCurrentMillisFixed(dtf.parseDateTime(comeOutTime).getMillis());
        eventLog.add ((new ExitEvent(vehicle)));

       // return eventLog;
        //List<ZoneBoundaryCrossing>
    }


}
