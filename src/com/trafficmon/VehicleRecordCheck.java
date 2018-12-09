package com.trafficmon;

import java.util.List;

public class VehicleRecordCheck {

    public boolean checkOrderingOf(List<ZoneBoundaryCrossing> crossings, Vehicle vehicle) {

        ZoneBoundaryCrossing lastEvent = crossings.get(0);

        for (ZoneBoundaryCrossing crossing : crossings.subList(1, crossings.size())) {
            if (crossing.timestamp() < lastEvent.timestamp()
                    || crossing instanceof EntryEvent && lastEvent instanceof EntryEvent
                    || crossing instanceof ExitEvent && lastEvent instanceof ExitEvent ) {
                OperationsTeam.getInstance().triggerInvestigationInto(vehicle);
                return false;
            }
            lastEvent = crossing;
        }

        return true;
    }

}
